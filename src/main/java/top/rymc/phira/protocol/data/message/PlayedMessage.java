package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class PlayedMessage extends Message {

    private final int user;
    private final int score;
    private final float accuracy;
    private final boolean fullCombo;

    public static PlayedMessage decode(ByteBuf buf) {
        return new PlayedMessage(
                buf.readIntLE(),
                buf.readIntLE(),
                buf.readFloatLE(),
                buf.readBoolean()
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        super.encode(buf);
        PacketWriter.write(buf, user);
        PacketWriter.write(buf, score);
        PacketWriter.write(buf, accuracy);
        PacketWriter.write(buf, fullCombo);
    }

}
