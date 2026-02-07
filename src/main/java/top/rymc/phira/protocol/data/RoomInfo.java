package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RoomInfo implements Encodeable {

    private final String roomId;
    private final GameState state;
    private final boolean live;
    private final boolean locked;
    private final boolean cycle;
    private final boolean isHost;
    private final boolean isReady;
    private final List<FullUserProfile> users;

    public RoomInfo(String roomId, GameState state, boolean live, boolean locked, boolean cycle, boolean isHost, boolean isReady, List<UserProfile> users, List<UserProfile> monitors) {
        this(roomId, state, live, locked, cycle, isHost, isReady, FullUserProfile.fromLists(users, monitors));
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, roomId);
        PacketWriter.write(buf, state);
        PacketWriter.write(buf, live);
        PacketWriter.write(buf, locked);
        PacketWriter.write(buf, cycle);
        PacketWriter.write(buf, isHost);
        PacketWriter.write(buf, isReady);

        // Here in the protocol, it's a Map<Integer, FullUserProfile>.
        // The Integer represents the user id in FullUserProfile.
        // Frankly, this is redundant data transmission, utterly pointless and only increases implementation costs.
        int size = users.size();
        PacketWriter.writeVarInt(buf, size);
        for (FullUserProfile user : users) {
            PacketWriter.write(buf, user.getUserId());
            PacketWriter.write(buf, user);
        }
    }

    public static RoomInfo decode(ByteBuf buf) {
        String roomId = NettyPacketUtil.decodeString(buf, 20);
        GameState state = GameState.decode(buf);
        boolean live = buf.readBoolean();
        boolean locked = buf.readBoolean();
        boolean cycle = buf.readBoolean();
        boolean isHost = buf.readBoolean();
        boolean isReady = buf.readBoolean();
        List<FullUserProfile> users = new ArrayList<>();
        int size = NettyPacketUtil.decodeVarInt(buf);
        for (int i = 0; i < size; i++) {
            buf.readIntLE(); // Drop
            users.add(FullUserProfile.decode(buf));
        }

        return new RoomInfo(roomId, state, live, locked, cycle, isHost, isReady, users);
    }
}
