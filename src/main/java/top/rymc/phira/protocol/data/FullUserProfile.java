package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FullUserProfile implements Encodeable {

    private final UserProfile userProfile;
    private final boolean monitor;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userProfile);
        PacketWriter.write(buf, monitor);
    }

    public static List<FullUserProfile> fromLists(List<UserProfile> users, List<UserProfile> monitors) {
        List<FullUserProfile> fullProfiles = new ArrayList<>(users.size() + monitors.size());
        for (UserProfile user : users) {
            fullProfiles.add(new FullUserProfile(user, false));
        }
        for (UserProfile monitor : monitors) {
            fullProfiles.add(new FullUserProfile(monitor, true));
        }
        return fullProfiles;
    }

}
