package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class CycleRoomMessage extends Message {

    private final boolean cycle;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, getMessageId());
        PacketWriter.write(buf, cycle);
    }

    @Override
    public int getMessageId() {
        return 0x0F;
    }
}
