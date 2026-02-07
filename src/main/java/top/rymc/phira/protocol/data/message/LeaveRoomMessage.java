package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class LeaveRoomMessage extends Message {

    private final int user;
    private final String name;

    public static LeaveRoomMessage decode(ByteBuf buf) {
        return new LeaveRoomMessage(
                buf.readIntLE(),
                NettyPacketUtil.decodeString(buf)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, user);
        PacketWriter.write(buf, name);
    }

}
