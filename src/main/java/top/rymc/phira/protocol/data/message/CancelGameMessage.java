package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class CancelGameMessage extends Message {

    private final int user;

    public static CancelGameMessage decode(ByteBuf buf) {
        return new CancelGameMessage(buf.readIntLE());
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, user);
    }

}