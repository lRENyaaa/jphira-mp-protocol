package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.FullUserProfile;
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
        private final List<FullUserProfile> users;
        private final boolean isLive;

        public Success(GameState gameState, List<UserProfile> users, List<UserProfile> monitors, boolean isLive) {
            this(gameState, FullUserProfile.fromLists(users, monitors), isLive);
        }

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.SUCCESS);
            PacketWriter.write(buf, gameState);
            PacketWriter.write(buf, users);
            PacketWriter.write(buf, isLive);
        }
    }


}
