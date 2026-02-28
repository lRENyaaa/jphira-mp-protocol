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
public class ClientBoundCycleRoomPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundCycleRoomPacket success() {
        return new ClientBoundCycleRoomPacket(PacketResult.successVoid());
    }

    public static ClientBoundCycleRoomPacket failed(String failedMessage) {
        return new ClientBoundCycleRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundCycleRoomPacket decode(ByteBuf buf) {
        return new ClientBoundCycleRoomPacket(PacketResult.decodeVoid(buf));
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
