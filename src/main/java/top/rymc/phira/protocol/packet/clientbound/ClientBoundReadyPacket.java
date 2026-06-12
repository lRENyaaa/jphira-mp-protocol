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
public class ClientBoundReadyPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundReadyPacket success() {
        return new ClientBoundReadyPacket(PacketResult.successVoid());
    }

    public static ClientBoundReadyPacket success(byte[] trailer) {
        return success().setTrailer(trailer, ClientBoundReadyPacket.class);
    }

    public static ClientBoundReadyPacket failed(String failedMessage) {
        return new ClientBoundReadyPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundReadyPacket failed(String failedMessage, byte[] trailer) {
        return failed(failedMessage).setTrailer(trailer, ClientBoundReadyPacket.class);
    }

    public static ClientBoundReadyPacket decode(ByteBuf buf) {
        return new ClientBoundReadyPacket(PacketResult.decodeVoid(buf)).setTrailer(buf, ClientBoundReadyPacket.class);
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
