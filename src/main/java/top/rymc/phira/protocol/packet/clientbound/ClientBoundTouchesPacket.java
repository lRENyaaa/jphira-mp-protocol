package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundTouchesPacket extends ClientBoundPacket {

    private final int id;
    private final List<TouchFrame> frames;

    public static ClientBoundTouchesPacket create(int id, List<TouchFrame> frames) {
        return new ClientBoundTouchesPacket(id, frames);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, frames);
    }
}
