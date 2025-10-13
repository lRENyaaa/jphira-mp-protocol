package top.rymc.phira.protocol.packet;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.handler.PacketHandler;
import top.rymc.phira.protocol.codec.Decodeable;

public abstract class ServerBoundPacket implements Decodeable {

    @Override
    public abstract void decode(ByteBuf buf);

    public abstract void handle(PacketHandler handler);

}
