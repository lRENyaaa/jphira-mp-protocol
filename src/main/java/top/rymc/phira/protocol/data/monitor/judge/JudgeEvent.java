package top.rymc.phira.protocol.data.monitor.judge;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
@RequiredArgsConstructor
public class JudgeEvent implements Encodeable {

    private final float time;
    private final int lineId;
    private final int noteId;
    private final Judgement judgement;

    public static JudgeEvent decode(ByteBuf buf) {
        return new JudgeEvent(
                buf.readFloatLE(),
                buf.readIntLE(),
                buf.readIntLE(),
                Judgement.decode(buf)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, time);
        PacketWriter.write(buf, lineId);
        PacketWriter.write(buf, noteId);
        PacketWriter.write(buf, judgement);
    }
}
