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

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, getMessageId());
        PacketWriter.write(buf, user);
        PacketWriter.write(buf, score);
        PacketWriter.write(buf, accuracy);
        PacketWriter.write(buf, fullCombo);
    }

    @Override
    public int getMessageId() {
        return 0x0B;
    }
}
