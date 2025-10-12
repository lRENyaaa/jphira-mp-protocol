package top.rymc.phira.protocol.codec;

import io.netty.buffer.ByteBuf;

public interface Encodeable {

    void encode(ByteBuf buf);

}
