package top.rymc.phira.protocol;

import lombok.Getter;
import top.rymc.phira.protocol.packet.serverbound.*;

@Getter
public abstract class PacketHandler {

    public abstract void handle(ServerBoundPingPacket packet);

    public abstract void handle(ServerBoundAuthenticatePacket packet);

    public abstract void handle(ServerBoundChatPacket packet);

    public abstract void handle(ServerBoundTouchesPacket packet);

    public abstract void handle(ServerBoundJudgesPacket packet);

    public abstract void handle(ServerBoundCreateRoomPacket packet);

    public abstract void handle(ServerBoundJoinRoomPacket packet);

    public abstract void handle(ServerBoundLeaveRoomPacket packet);

    public abstract void handle(ServerBoundLockRoomPacket packet);

    public abstract void handle(ServerBoundCycleRoomPacket packet);

    public abstract void handle(ServerBoundSelectChartPacket packet);

    public abstract void handle(ServerBoundRequestStartPacket packet);

    public abstract void handle(ServerBoundReadyPacket packet);

    public abstract void handle(ServerBoundCancelReadyPacket packet);

    public abstract void handle(ServerBoundPlayedPacket packet);

    public abstract void handle(ServerBoundAbortPacket packet);

}

