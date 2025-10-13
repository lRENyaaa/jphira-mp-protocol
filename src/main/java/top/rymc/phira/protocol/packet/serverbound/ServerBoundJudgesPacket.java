package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;

@Getter
public class ServerBoundJudgesPacket extends ServerBoundPacket {

    private byte[] data;

    @Override
    public void decode(ByteBuf buf) {
        data = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), data);
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handle(this);
    }
}
