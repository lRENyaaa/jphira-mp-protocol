package top.rymc.phira.protocol.data.monitor.judge;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public enum Judgement implements Encodeable {
    Perfect(0x00),
    Good(0x01),
    Bad(0x02),
    Miss(0x03),
    HoldPerfect(0x04),
    HoldGood(0x05);

    private final int id;

    private static Map<Integer,Judgement> getJudgementMap() {
        return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                Judgement::getId,
                Function.identity()
        )));
    }

    private static final Map<Integer,Judgement> JUDGEMENT_MAP = getJudgementMap();

    public static Judgement decode(ByteBuf buf) {
        int id = buf.readByte();
        Judgement judgement = JUDGEMENT_MAP.get(id);
        if (judgement == null) {
            throw new DecoderException("Unknown Judgement id: " + id);
        }
        return judgement;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, id);
    }
}
