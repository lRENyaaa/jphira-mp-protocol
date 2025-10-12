package top.rymc.phira.protocol.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;

public class FrameEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        ByteBuf lenBuf = ctx.alloc().buffer(5);

        NettyPacketUtil.writeVarInt(lenBuf, length);
        out.add(lenBuf);
        out.add(msg.retain());
    }
}
