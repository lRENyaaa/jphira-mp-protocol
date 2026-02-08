package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundCreateRoomPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundCreateRoomPacket success() {
        return new ClientBoundCreateRoomPacket(PacketResult.success(null));
    }

    public static ClientBoundCreateRoomPacket failed(String failedMessage) {
        return new ClientBoundCreateRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundCreateRoomPacket decode(ByteBuf buf) {
        return new ClientBoundCreateRoomPacket(PacketResult.decodeVoid(buf));
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
