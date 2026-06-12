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
public class ClientBoundChatPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundChatPacket success() {
        return new ClientBoundChatPacket(PacketResult.successVoid());
    }

    public static ClientBoundChatPacket success(byte[] trailer) {
        return success().setTrailer(trailer, ClientBoundChatPacket.class);
    }

    public static ClientBoundChatPacket failed(String failedMessage) {
        return new ClientBoundChatPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundChatPacket failed(String failedMessage, byte[] trailer) {
        return failed(failedMessage).setTrailer(trailer, ClientBoundChatPacket.class);
    }

    public static ClientBoundChatPacket decode(ByteBuf buf) {
        return new ClientBoundChatPacket(PacketResult.decodeVoid(buf)).setTrailer(buf, ClientBoundChatPacket.class);
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
