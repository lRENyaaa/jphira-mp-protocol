package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class AbortMessage extends Message {

    private final int user;

    public static AbortMessage decode(ByteBuf buf) {
        return new AbortMessage(buf.readIntLE());
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, user);
    }

}
