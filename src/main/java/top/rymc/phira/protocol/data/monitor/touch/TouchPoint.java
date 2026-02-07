package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor
public class TouchPoint implements Encodeable {

    private final byte id;
    private final CompactPos pos;

    public static TouchPoint decode(ByteBuf buf) {
        return new TouchPoint(
                buf.readByte(),
                CompactPos.decode(buf)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, pos);
    }
}
