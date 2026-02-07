package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor
public class CompactPos implements Encodeable {

    private final float x;
    private final float y;

    public static CompactPos decode(ByteBuf buf) {
        return new CompactPos(
                NettyPacketUtil.decodeFloat16LE(buf),
                NettyPacketUtil.decodeFloat16LE(buf)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeFloat16(buf, x);
        PacketWriter.writeFloat16(buf, y);
    }
}
