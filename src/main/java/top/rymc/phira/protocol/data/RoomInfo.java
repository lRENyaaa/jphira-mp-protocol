package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.util.PacketWriter;

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
        PacketWriter.write(buf, users);
    }
}
