package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundChangeStatePacket extends ClientBoundPacket {

    private final GameState gameState;

    public static ClientBoundChangeStatePacket create(GameState gameState) {
        return new ClientBoundChangeStatePacket(gameState);
    }

    public static ClientBoundChangeStatePacket decode(ByteBuf buf) {
        return new ClientBoundChangeStatePacket(GameState.decode(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, gameState);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
    