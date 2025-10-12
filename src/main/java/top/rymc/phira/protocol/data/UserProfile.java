package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

public record UserProfile(int userId, String username) implements Encodeable {

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userId);
        PacketWriter.write(buf, username);
    }
}
