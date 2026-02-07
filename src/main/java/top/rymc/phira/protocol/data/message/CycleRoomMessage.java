package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class CycleRoomMessage extends Message {

    private final boolean cycle;

    public static CycleRoomMessage decode(ByteBuf buf) {
        return new CycleRoomMessage(buf.readBoolean());
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, cycle);
    }

}
