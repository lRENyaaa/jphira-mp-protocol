package top.rymc.phira.protocol.data.monitor.judge;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public enum Judgement implements Encodeable {
    Perfect(0x00),
    Good(0x01),
    Bad(0x02),
    Miss(0x03),
    HoldPerfect(0x04),
    HoldGood(0x05);

    private final int code;

    public static Judgement decode(ByteBuf buf) {
        return get(buf.readByte());
    }

    public static Judgement get(int code) {
        for (Judgement judgement : Judgement.values()) {
            if (judgement.code != code) {
                continue;
            }

            return judgement;
        }
        throw new DecoderException("Unknown judgement code: " + code);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, code);
    }
}
