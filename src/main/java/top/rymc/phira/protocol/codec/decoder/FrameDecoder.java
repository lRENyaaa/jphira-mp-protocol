package top.rymc.phira.protocol.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCountUtil;
import top.rymc.phira.protocol.exception.BadVarIntException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FrameDecoder extends ByteToMessageDecoder {

    private static final Set<Integer> SUPPORTED_VERSIONS = Set.of(0x01);

    private HandleFunction currentHandler = this::clientVersionHandshakeHandle;

    private final long timeout;
    private final TimeUnit timeunit;

    public FrameDecoder() {
        this(5000, TimeUnit.MILLISECONDS);
    }

    public FrameDecoder(long timeout, TimeUnit timeunit) {
        this.timeout = timeout;
        this.timeunit = timeunit;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) {
            in.clear();
            return;
        }

        currentHandler.handle(ctx, in, out);

    }

    private final CompletableFuture<Integer> clientProtocolVersionPromise = new CompletableFuture<>();
    private ScheduledFuture<?> handshakeTimeout;

    @Override
    @SuppressWarnings("resource")
    public void channelActive(ChannelHandlerContext ctx) {
        handshakeTimeout = ctx.executor().schedule(() -> {
            if (clientProtocolVersionPromise.isDone()) {
                return;
            }

            clientProtocolVersionPromise.completeExceptionally(new TimeoutException("Handshake timeout: no data received"));
            ctx.close();
        }, timeout, timeunit);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (handshakeTimeout != null) {
            handshakeTimeout.cancel(false);
        }
        super.channelInactive(ctx);
    }

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

        handshakeTimeout.cancel(false);
        clientProtocolVersionPromise.complete(clientProtocolVersion);

        currentHandler = this::handle;

        if (in.isReadable()) {
            handle(ctx, in, out);
        }

    }

    private void handle(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int skip = in.forEachByte(ByteProcessor.FIND_NON_NUL);

        if (skip < 0) {
            in.clear();
            return;
        }

        in.readerIndex(skip);
        in.markReaderIndex();

        int length;
        try {
            length = NettyPacketUtil.decodeVarInt(in);
        } catch (NeedMoreDataException e) {
            in.resetReaderIndex();
            return;
        } catch (BadVarIntException e) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            return;
        }

        if (length < 0) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            throw new CorruptedFrameException("Bad packet length");
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


