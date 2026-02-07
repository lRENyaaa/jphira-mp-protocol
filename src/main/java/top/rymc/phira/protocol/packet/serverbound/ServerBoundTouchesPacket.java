package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ServerBoundTouchesPacket extends ServerBoundPacket {

    private final List<TouchFrame> frames;

    public static ServerBoundTouchesPacket decode(ByteBuf buf) {
        return new ServerBoundTouchesPacket(
                NettyPacketUtil.decodeList(buf, TouchFrame::decode)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, frames);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
