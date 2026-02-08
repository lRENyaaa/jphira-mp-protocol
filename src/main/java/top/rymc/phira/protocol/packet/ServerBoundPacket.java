package top.rymc.phira.protocol.packet;

import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;

public abstract class ServerBoundPacket implements Encodeable {

    public abstract void handle(ServerBoundPacketHandler handler);

}
