package top.rymc.phira.protocol.packet.clientbound;


import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.FullUserProfile;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundOnJoinRoomPacket extends ClientBoundPacket {

    private final FullUserProfile userProfile;

    public static ClientBoundOnJoinRoomPacket create(FullUserProfile userProfile) {
        return new ClientBoundOnJoinRoomPacket(userProfile);
    }

    public static ClientBoundOnJoinRoomPacket create(UserProfile userProfile, boolean isMonitor) {
        return create(new FullUserProfile(userProfile, isMonitor));
    }

    public static ClientBoundOnJoinRoomPacket decode(ByteBuf buf) {
        return new ClientBoundOnJoinRoomPacket(FullUserProfile.decode(buf));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userProfile);
    }
}
