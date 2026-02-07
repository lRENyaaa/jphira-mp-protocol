package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class CancelReadyMessage extends Message {

    private final int user;

    public static CancelReadyMessage decode(ByteBuf buf) {
        return new CancelReadyMessage(buf.readIntLE());
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, user);
    }

}