package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class GameEndMessage extends Message {

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, getMessageId());
    }

    @Override
    public int getMessageId() {
        return 0x0C;
    }
}
