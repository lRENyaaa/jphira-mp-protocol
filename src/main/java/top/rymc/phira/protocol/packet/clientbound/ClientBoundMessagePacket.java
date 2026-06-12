package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.message.Message;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ClientBoundMessagePacket extends ClientBoundPacket {

    private final Message message;

    public static ClientBoundMessagePacket create(Message message) {
        return new ClientBoundMessagePacket(message);
    }

    public static ClientBoundMessagePacket create(Message message, byte[] trailer) {
        return create(message).setTrailer(trailer, ClientBoundMessagePacket.class);
    }

    public static ClientBoundMessagePacket decode(ByteBuf buf) {
        return new ClientBoundMessagePacket(Message.decode(buf)).setTrailer(buf, ClientBoundMessagePacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, message);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
