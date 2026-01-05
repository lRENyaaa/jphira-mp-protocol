package top.rymc.phira.protocol.data.monitor.judge;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Decodeable;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
public class JudgeEvent implements Encodeable, Decodeable {

    private float time;
    private int lineId;
    private int noteId;
    private Judgement judgement;

    public JudgeEvent() {
        // Empty constructor
    }

    public JudgeEvent(float time, int lineId, int noteId, Judgement judgement) {
        this.time = time;
        this.lineId = lineId;
        this.noteId = noteId;
        this.judgement = judgement;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.time = buf.readFloatLE();
        this.lineId = buf.readIntLE();
        this.noteId = buf.readIntLE();
        this.judgement = Judgement.get(buf.readByte());
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, time);
        PacketWriter.write(buf, lineId);
        PacketWriter.write(buf, noteId);
        PacketWriter.write(buf, judgement);
    }
}
