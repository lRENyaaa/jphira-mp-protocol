package top.rymc.phira.protocol.data.state;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.util.PacketWriter;

public final class Playing extends GameState {

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, 0x02);
    }

    @Override
    public String toString() {
        return "Playing";
    }


}
