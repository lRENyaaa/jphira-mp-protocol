package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.judge.JudgeEvent;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundJudgesPacket extends ServerBoundPacket {

    private final List<JudgeEvent> judges;

    public static ServerBoundJudgesPacket decode(ByteBuf buf) {
        return new ServerBoundJudgesPacket(NettyPacketUtil.decodeList(buf, JudgeEvent::decode));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, judges);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
