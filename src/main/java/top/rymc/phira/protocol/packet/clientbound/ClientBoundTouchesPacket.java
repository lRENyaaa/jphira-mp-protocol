package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@RequiredArgsConstructor
public class ClientBoundTouchesPacket extends ClientBoundPacket {

    private final int id;
    private final List<TouchFrame> frames;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, frames);
    }
}
