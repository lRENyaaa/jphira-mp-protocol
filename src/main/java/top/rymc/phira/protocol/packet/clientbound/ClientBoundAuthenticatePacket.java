package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.data.RoomInfo;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

public abstract class ClientBoundAuthenticatePacket extends ClientBoundPacket {

    @RequiredArgsConstructor
    public static class Failed extends ClientBoundAuthenticatePacket {

        private final String reason;

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.FAILED);
            PacketWriter.write(buf, reason);
        }

    }

    @RequiredArgsConstructor
    public static class Success extends ClientBoundAuthenticatePacket {

        private final UserProfile userProfile;
        private final boolean isMonitor;
        private final RoomInfo roomInfo;

        public Success(UserProfile userProfile, boolean isMonitor) {
            this(userProfile, isMonitor, null);
        }

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, PacketResult.SUCCESS);
            PacketWriter.write(buf, userProfile);

            // Monitor status returned during authentication.
            // Although not explicitly documented, this field has been deprecated.
            // The actual monitor status depends on the packet sent when the player joins a room.
            PacketWriter.write(buf, isMonitor);

            boolean hasRoomInfo = roomInfo != null;
            PacketWriter.write(buf, hasRoomInfo);
            if (hasRoomInfo) {
                PacketWriter.write(buf, roomInfo);
            }

        }

    }


}
