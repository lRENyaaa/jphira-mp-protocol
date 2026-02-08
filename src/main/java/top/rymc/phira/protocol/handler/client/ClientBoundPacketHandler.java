package top.rymc.phira.protocol.handler.client;

import top.rymc.phira.protocol.packet.clientbound.*;

public abstract class ClientBoundPacketHandler {

    public abstract void handle(ClientBoundPongPacket packet);

    public abstract void handle(ClientBoundAuthenticatePacket packet);

    public abstract void handle(ClientBoundChatPacket packet);

    public abstract void handle(ClientBoundTouchesPacket packet);

    public abstract void handle(ClientBoundJudgesPacket packet);

    public abstract void handle(ClientBoundMessagePacket packet);

    public abstract void handle(ClientBoundChangeStatePacket packet);

    public abstract void handle(ClientBoundChangeHostPacket packet);

    public abstract void handle(ClientBoundCreateRoomPacket packet);

    public abstract void handle(ClientBoundJoinRoomPacket packet);

    public abstract void handle(ClientBoundOnJoinRoomPacket packet);

    public abstract void handle(ClientBoundLeaveRoomPacket packet);

    public abstract void handle(ClientBoundLockRoomPacket packet);

    public abstract void handle(ClientBoundCycleRoomPacket packet);

    public abstract void handle(ClientBoundSelectChartPacket packet);

    public abstract void handle(ClientBoundRequestStartPacket packet);

    public abstract void handle(ClientBoundReadyPacket packet);

    public abstract void handle(ClientBoundCancelReadyPacket packet);

    public abstract void handle(ClientBoundPlayedPacket packet);

    public abstract void handle(ClientBoundAbortPacket packet);

}
