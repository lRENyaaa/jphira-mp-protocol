package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Decodeable;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
public class TouchPoint implements Encodeable, Decodeable {

    private byte id;
    private CompactPos pos;

    public TouchPoint() {
        // Empty constructor
    }

    public TouchPoint(byte id, CompactPos pos) {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public void decode(ByteBuf buf) {
        id = buf.readByte();
        pos = new CompactPos();
        pos.decode(buf);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, pos);
    }
}
