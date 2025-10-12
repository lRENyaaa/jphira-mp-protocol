package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.message.Message;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class ClientBoundMessagePacket extends ClientBoundPacket {

    private final Message message;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, message);
    }

}
