package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.FullUserProfile;
import top.rymc.phira.protocol.data.PacketResult;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientBoundJoinRoomPacket extends ClientBoundPacket {

    private final PacketResult<Data> result;

    public static ClientBoundJoinRoomPacket success(GameState gameState, List<UserProfile> users, List<UserProfile> monitors, boolean isLive) {
        return success(gameState, FullUserProfile.fromLists(users, monitors), isLive);
    }

    public static ClientBoundJoinRoomPacket success(GameState gameState, List<FullUserProfile> users, boolean isLive) {
        return new ClientBoundJoinRoomPacket(PacketResult.success(new Data(gameState, users, isLive)));
    }

    public static ClientBoundJoinRoomPacket failed(String failedMessage) {
        return new ClientBoundJoinRoomPacket(PacketResult.failed(failedMessage));
    }

    public static ClientBoundJoinRoomPacket decode(ByteBuf buf) {
        return new ClientBoundJoinRoomPacket(PacketResult.decode(buf, Data::decode ));
    }

    @Override
    public void encode(ByteBuf buf) {
        result.encode(buf);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }

    private record Data(GameState gameState, List<FullUserProfile> users, boolean isLive) implements Encodeable {

        @Override
        public void encode(ByteBuf buf) {
            PacketWriter.write(buf, gameState);
            PacketWriter.write(buf, users);
            PacketWriter.write(buf, isLive);
        }

        public static Data decode(ByteBuf buf) {
             return new Data(
                    GameState.decode(buf),
                    NettyPacketUtil.decodeList(buf, FullUserProfile::decode),
                    buf.readBoolean()
            );
        }
    }
}
