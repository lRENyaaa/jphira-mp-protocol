package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Decodeable;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
public class CompactPos implements Encodeable, Decodeable {

    private float x;
    private float y;

    public CompactPos() {
       // Empty constructor
    }

    public CompactPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.x = NettyPacketUtil.decodeFloat16LE(buf);
        this.y = NettyPacketUtil.decodeFloat16LE(buf);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeFloat16(buf, x);
        PacketWriter.writeFloat16(buf, y);
    }
}
