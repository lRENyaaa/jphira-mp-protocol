package top.rymc.phira.protocol.packet;

import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;

public abstract class ClientBoundPacket extends Packet {

    public abstract void handle(ClientBoundPacketHandler handler);
}
