package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.judge.JudgeEvent;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundJudgesPacket extends ClientBoundPacket {

    private final int id;
    private final List<JudgeEvent> judges;

    public static ClientBoundJudgesPacket create(int id, List<JudgeEvent> judges) {
        return new ClientBoundJudgesPacket(id, judges);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, judges);
    }
}
