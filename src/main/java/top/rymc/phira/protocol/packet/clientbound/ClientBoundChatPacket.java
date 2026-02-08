package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundChatPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundChatPacket success() {
        return new ClientBoundChatPacket(PacketResult.success(null));
    }

    public static ClientBoundChatPacket failed(String failedMessage) {
        return new ClientBoundChatPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundChatPacket decode(ByteBuf buf) {
        return new ClientBoundChatPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
