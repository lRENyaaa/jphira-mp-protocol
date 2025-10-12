package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

public abstract class ClientBoundJoinRoomPacket extends ClientBoundPacket {


    @RequiredArgsConstructor
    public static class Failed extends ClientBoundJoinRoomPacket {

        private final String reason;

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.FAILED);
            PacketWriter.write(buf, reason);
        }

    }

    @RequiredArgsConstructor
    public static class Success extends ClientBoundJoinRoomPacket {

        private final GameState gameState;
        private final List<UserProfile> users;
        private final List<UserProfile> monitors;
        private final boolean isLive;

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.SUCCESS);
            PacketWriter.write(buf, gameState);
            PacketWriter.writeByte(buf, users.size() + monitors.size());
            for (UserProfile user : users) {
                PacketWriter.write(buf, user);
                PacketWriter.write(buf, false);
            }
            for (UserProfile monitor : monitors) {
                PacketWriter.write(buf, monitor);
                PacketWriter.write(buf, true);
            }
            PacketWriter.write(buf, isLive);
        }
    }


}
