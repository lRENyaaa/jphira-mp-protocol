package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.FullUserProfile;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.data.RoomInfo;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundAuthenticatePacket extends ClientBoundPacket {

    private final PacketResult<Data> result;

    public static ClientBoundAuthenticatePacket success(FullUserProfile userProfile) {
        return success(userProfile, null);
    }

    public static ClientBoundAuthenticatePacket success(FullUserProfile userProfile, RoomInfo roomInfo) {
        return new ClientBoundAuthenticatePacket(PacketResult.success(new Data(userProfile, roomInfo)));
    }

    public static ClientBoundAuthenticatePacket failed(String failedMessage) {
        return new ClientBoundAuthenticatePacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundAuthenticatePacket decode(ByteBuf buf) {
        return new ClientBoundAuthenticatePacket(PacketResult.decode(buf, Data::decode));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, result);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }

    private record Data(FullUserProfile userProfile, RoomInfo roomInfo) implements Encodeable {
        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, userProfile);

            boolean hasRoomInfo = roomInfo != null;
            PacketWriter.write(buf, hasRoomInfo);
            if (hasRoomInfo) {
                PacketWriter.write(buf, roomInfo);
            }
        }

        public static Data decode(ByteBuf buf) {
            FullUserProfile userProfile = FullUserProfile.decode(buf);
            RoomInfo roomInfo = null;
            if (buf.readBoolean()) {
                roomInfo = RoomInfo.decode(buf);
            }

            return new Data(userProfile, roomInfo);
        }
    }

}
