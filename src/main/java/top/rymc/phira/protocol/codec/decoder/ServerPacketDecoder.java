package top.rymc.phira.protocol.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.CodecException;
import io.netty.util.ReferenceCountUtil;
import top.rymc.phira.protocol.PacketRegistry;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

import java.util.function.Consumer;

public class ServerPacketDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;

        try {
            if (!ctx.channel().isActive() || !buf.isReadable()) {
                return;
            }

            ServerBoundPacket packet = tryDecode(buf, e -> ctx.close());

            if (packet == null) {
                return;
            }

            ctx.fireChannelRead(packet);
        } finally {
            ReferenceCountUtil.safeRelease(buf);
        }

    }

    private static ServerBoundPacket tryDecode(ByteBuf buf, Consumer<CodecException> onException) {
        try {
            return PacketRegistry.ServerBound.decode(buf);
        } catch (CodecException e) {
            onException.accept(e);
            return null;
        }
    }
}