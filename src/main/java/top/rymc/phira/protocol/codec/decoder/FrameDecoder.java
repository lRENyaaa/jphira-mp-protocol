package top.rymc.phira.protocol.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.ReferenceCountUtil;
import top.rymc.phira.protocol.exception.BadVarintException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FrameDecoder extends ByteToMessageDecoder {

    private static final Set<Integer> SUPPORTED_VERSIONS = Set.of(0x01);
    private static final CorruptedFrameException BAD_PACKET_LENGTH = new CorruptedFrameException("Bad packet length");

    private HandleFunction currentHandler = this::clientVersionHandshakeHandle;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) {
            in.clear();
            return;
        }

        currentHandler.handle(ctx, in, out);

    }

    private final CompletableFuture<Integer> clientProtocolVersionPromise = new CompletableFuture<>();

    public CompletableFuture<Integer> getClientProtocolVersion() {
        return clientProtocolVersionPromise;
    }

    private void clientVersionHandshakeHandle(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!in.isReadable()) return;
        int clientProtocolVersion = in.readByte();

        if (!SUPPORTED_VERSIONS.contains(clientProtocolVersion)) {
            clientProtocolVersionPromise.completeExceptionally(new IllegalStateException("Unsupported protocol version: " + clientProtocolVersion));
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
        }

        clientProtocolVersionPromise.complete(clientProtocolVersion);

        currentHandler = this::handle;

        if (in.isReadable()) {
            handle(ctx, in, out);
        }

    }

    private void handle(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();

        int length;
        try {
            length = NettyPacketUtil.readVarInt(in);
        } catch (NeedMoreDataException e) {
            in.resetReaderIndex();
            return;
        } catch (BadVarintException e) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            return;
        }

        if (length < 0) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            throw BAD_PACKET_LENGTH;
        }

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf frame = in.readRetainedSlice(length);
        out.add(frame);
    }

    @FunctionalInterface
    private interface HandleFunction {
        void handle(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);
    }

}


