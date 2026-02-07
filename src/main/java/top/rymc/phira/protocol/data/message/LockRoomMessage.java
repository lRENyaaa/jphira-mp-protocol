package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class LockRoomMessage extends Message {

    private final boolean lock;

    public static LockRoomMessage decode(ByteBuf buf) {
        return new LockRoomMessage(buf.readBoolean());
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, lock);
    }
}
