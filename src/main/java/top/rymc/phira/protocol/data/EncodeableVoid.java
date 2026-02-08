package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;

public final class EncodeableVoid implements Encodeable {

    private EncodeableVoid() {}

    @Override
    public void encode(ByteBuf buf) {}
}
