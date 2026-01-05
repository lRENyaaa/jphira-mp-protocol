package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.data.monitor.judge.JudgeEvent;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;

@Getter
public class ServerBoundJudgesPacket extends ServerBoundPacket {

    private List<JudgeEvent> judges;

    @Override
    public void decode(ByteBuf buf) {
        judges = NettyPacketUtil.decodeList(buf, JudgeEvent::new);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
