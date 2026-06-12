package top.rymc.phira.protocol.packet;

import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;

public abstract class ServerBoundPacket extends Packet  {

    public abstract void handle(ServerBoundPacketHandler handler);

}
