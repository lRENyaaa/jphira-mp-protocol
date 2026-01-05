package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;

@Getter
public class ServerBoundTouchesPacket extends ServerBoundPacket {

    private List<TouchFrame> frames;

    @Override
    public void decode(ByteBuf buf) {
        frames = NettyPacketUtil.decodeList(buf, TouchFrame::new);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
