package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

public abstract class ClientBoundSelectChartPacket extends ClientBoundPacket {

    @RequiredArgsConstructor
    public static class Failed extends ClientBoundSelectChartPacket {

        private final String reason;

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.FAILED);
            PacketWriter.write(buf, reason);
        }

    }

    @RequiredArgsConstructor
    public static class Success extends ClientBoundSelectChartPacket {

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.SUCCESS);
        }

    }
}
