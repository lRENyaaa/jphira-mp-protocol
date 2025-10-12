package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class LeaveRoomMessage extends Message {

    private final int user;
    private final String name;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, getMessageId());
        PacketWriter.write(buf, user);
        PacketWriter.write(buf, name);
    }

    @Override
    public int getMessageId() {
        return 0x03;
    }
}
