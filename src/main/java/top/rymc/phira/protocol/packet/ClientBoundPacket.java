package top.rymc.phira.protocol.packet;

import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;

public abstract class ClientBoundPacket implements Encodeable {

    public abstract void handle(ClientBoundPacketHandler handler);
}
