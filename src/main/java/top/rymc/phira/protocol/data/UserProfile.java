package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@Getter
public final class UserProfile implements Encodeable {

    private final int userId;
    private final String userName;

    public UserProfile(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userId);
        PacketWriter.write(buf, userName);
    }

    public static UserProfile decode(ByteBuf buf) {
        return new UserProfile(
                buf.readIntLE(),
                NettyPacketUtil.decodeString(buf)
        );
    }

}
