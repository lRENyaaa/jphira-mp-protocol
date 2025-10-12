package top.rymc.phira.protocol.packet;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;

public abstract class ClientBoundPacket implements Encodeable {

    @Override
    public abstract void encode(ByteBuf buf);

}
