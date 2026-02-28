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
public class ClientBoundSelectChartPacket extends ClientBoundPacket {

    private final PacketResult<EncodeableVoid> result;

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public String getFailedMessage() {
        return result.getFailedMessage();
    }

    public static ClientBoundSelectChartPacket success() {
        return new ClientBoundSelectChartPacket(PacketResult.successVoid());
    }

    public static ClientBoundSelectChartPacket failed(String failedMessage) {
        return new ClientBoundSelectChartPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundSelectChartPacket decode(ByteBuf buf) {
        return new ClientBoundSelectChartPacket(PacketResult.decodeVoid(buf));
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
