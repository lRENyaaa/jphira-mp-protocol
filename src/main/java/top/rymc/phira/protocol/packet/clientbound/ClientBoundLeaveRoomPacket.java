package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.EncodeableVoid;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundLeaveRoomPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public static ClientBoundLeaveRoomPacket success() {
        return new ClientBoundLeaveRoomPacket(PacketResult.successVoid());
    }

    public static ClientBoundLeaveRoomPacket failed(String failedMessage) {
        return new ClientBoundLeaveRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundLeaveRoomPacket decode(ByteBuf buf) {
        return new ClientBoundLeaveRoomPacket(PacketResult.decodeVoid(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, result);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
