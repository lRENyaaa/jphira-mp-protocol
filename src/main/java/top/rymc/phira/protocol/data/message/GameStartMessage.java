package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class GameStartMessage extends Message {

    private final int user;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, getMessageId());
        PacketWriter.write(buf, user);
    }

    @Override
    public int getMessageId() {
        return 0x06;
    }
}
