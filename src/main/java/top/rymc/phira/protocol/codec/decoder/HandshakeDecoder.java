package top.rymc.phira.protocol.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.ScheduledFuture;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HandshakeDecoder extends ByteToMessageDecoder {
    private static final Set<Integer> SUPPORTED_VERSIONS = Set.of(0x01);

    private final CompletableFuture<Integer> clientProtocolVersionPromise = new CompletableFuture<>();
    private final long timeout;
    private final TimeUnit timeUnit;

    private ScheduledFuture<?> timeoutTask;

    public HandshakeDecoder() {
        this(5000, TimeUnit.MILLISECONDS);
    }

    public HandshakeDecoder(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.timeUnit = unit;
    }

    public CompletableFuture<Integer> getClientProtocolVersion() {
        return clientProtocolVersionPromise;
    }

    @SuppressWarnings("resource")
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        timeoutTask = ctx.executor().schedule(() -> {
            if (!clientProtocolVersionPromise.isDone()) {
                clientProtocolVersionPromise.completeExceptionally(new TimeoutException("Handshake timeout"));
                ctx.close();
            }
        }, timeout, timeUnit);

        super.channelActive(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!in.isReadable()) return;

        int version = in.readByte();

        if (!SUPPORTED_VERSIONS.contains(version)) {
            cancelTimeout();
            clientProtocolVersionPromise.completeExceptionally(
                    new IllegalStateException("Unsupported version: " + version)
            );
            ctx.close();
            return;
        }

        clientProtocolVersionPromise.complete(version);
        cancelTimeout();

        if (in.readableBytes() > 0) {
            out.add(in.retain());
        }

        ctx.pipeline().remove(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cancelTimeout();
        if (!clientProtocolVersionPromise.isDone()) {
            clientProtocolVersionPromise.completeExceptionally(new IOException("Connection closed before handshake"));
        }
        super.channelInactive(ctx);
    }

    private void cancelTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
        }
    }
}
