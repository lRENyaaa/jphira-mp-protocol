package top.rymc.phira.protocol.packet.clientbound;


import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.FullUserProfile;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ClientBoundOnJoinRoomPacket extends ClientBoundPacket {

    private final FullUserProfile userProfile;

    public static ClientBoundOnJoinRoomPacket create(FullUserProfile userProfile) {
        return new ClientBoundOnJoinRoomPacket(userProfile);
    }

    public static ClientBoundOnJoinRoomPacket create(FullUserProfile userProfile, byte[] trailer) {
        return create(userProfile).setTrailer(trailer, ClientBoundOnJoinRoomPacket.class);
    }

    public static ClientBoundOnJoinRoomPacket create(UserProfile userProfile, boolean isMonitor) {
        return create(new FullUserProfile(userProfile, isMonitor));
    }

    public static ClientBoundOnJoinRoomPacket create(UserProfile userProfile, boolean isMonitor, byte[] trailer) {
        return create(userProfile, isMonitor).setTrailer(trailer, ClientBoundOnJoinRoomPacket.class);
    }

    public static ClientBoundOnJoinRoomPacket decode(ByteBuf buf) {
        return new ClientBoundOnJoinRoomPacket(FullUserProfile.decode(buf)).setTrailer(buf, ClientBoundOnJoinRoomPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userProfile);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
