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
public class ClientBoundAbortPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundAbortPacket success() {
        return new ClientBoundAbortPacket(PacketResult.successVoid());
    }

    public static ClientBoundAbortPacket success(byte[] trailer) {
        return success().setTrailer(trailer, ClientBoundAbortPacket.class);
    }

    public static ClientBoundAbortPacket failed(String failedMessage) {
        return new ClientBoundAbortPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundAbortPacket failed(String failedMessage, byte[] trailer) {
        return failed(failedMessage).setTrailer(trailer, ClientBoundAbortPacket.class);
    }

    public static ClientBoundAbortPacket decode(ByteBuf buf) {
        return new ClientBoundAbortPacket(PacketResult.decodeVoid(buf)).setTrailer(buf, ClientBoundAbortPacket.class);
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
