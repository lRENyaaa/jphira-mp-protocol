package top.rymc.phira.protocol.codec.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import top.rymc.phira.protocol.PacketRegistry;
import top.rymc.phira.protocol.packet.ClientBoundPacket;

public class PacketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientBoundPacket packet) {
            try {
                msg = PacketRegistry.encode(packet, () -> ctx.alloc().buffer());
            } catch (Exception e) {
                promise.setFailure(e);
                ctx.close();
                return;
            }
        }

        ctx.write(msg, promise);
    }


}
