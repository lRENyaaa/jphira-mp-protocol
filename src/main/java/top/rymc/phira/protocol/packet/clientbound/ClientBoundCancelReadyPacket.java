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
public class ClientBoundCancelReadyPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundCancelReadyPacket success() {
        return new ClientBoundCancelReadyPacket(PacketResult.successVoid());
    }

    public static ClientBoundCancelReadyPacket success(byte[] trailer) {
        return success().setTrailer(trailer, ClientBoundCancelReadyPacket.class);
    }

    public static ClientBoundCancelReadyPacket failed(String failedMessage) {
        return new ClientBoundCancelReadyPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundCancelReadyPacket failed(String failedMessage, byte[] trailer) {
        return failed(failedMessage).setTrailer(trailer, ClientBoundCancelReadyPacket.class);
    }

    public static ClientBoundCancelReadyPacket decode(ByteBuf buf) {
        return new ClientBoundCancelReadyPacket(PacketResult.decodeVoid(buf)).setTrailer(buf, ClientBoundCancelReadyPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, result);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
