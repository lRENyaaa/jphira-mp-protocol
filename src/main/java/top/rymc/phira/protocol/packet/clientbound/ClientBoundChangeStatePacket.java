package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class ClientBoundChangeStatePacket extends ClientBoundPacket {

    private final GameState gameState;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, gameState);
    }

}
    