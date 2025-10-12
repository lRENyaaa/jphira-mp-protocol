package top.rymc.phira.protocol.codec;

import io.netty.buffer.ByteBuf;

public interface Decodeable {

    void decode(ByteBuf buf);

}
