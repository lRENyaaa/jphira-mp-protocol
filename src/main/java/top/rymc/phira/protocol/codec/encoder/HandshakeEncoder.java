package top.rymc.phira.protocol.codec.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Set;

public class HandshakeEncoder extends ChannelInboundHandlerAdapter {

    private static final Set<Integer> SUPPORTED_VERSIONS = Set.of(0x01);

    private final int version;

    public HandshakeEncoder(int version) {
        if (!SUPPORTED_VERSIONS.contains(version)) {
            throw new IllegalStateException("Unsupported version: " + version);
        }
        this.version = version;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(ctx.alloc().buffer(1).writeByte(version))
                .addListener(future -> ctx.pipeline().remove(this));
        super.channelActive(ctx);
    }

}
