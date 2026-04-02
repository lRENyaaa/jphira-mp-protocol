package top.rymc.phira.protocol.handler.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.packet.clientbound.ClientBoundPongPacket;
import top.rymc.phira.protocol.packet.serverbound.*;

@RequiredArgsConstructor
@Getter
public abstract class SimpleServerBoundPacketHandler extends ServerBoundPacketHandler {

    protected abstract void sendPacket(ClientBoundPacket packet);

    @Override
    public void handle(ServerBoundPingPacket packet) {
        sendPacket(ClientBoundPongPacket.INSTANCE);
    }

    @Override
    public void handle(ServerBoundAuthenticatePacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundChatPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundTouchesPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundJudgesPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundCreateRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundJoinRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundLeaveRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundLockRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundCycleRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundSelectChartPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundRequestStartPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundReadyPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundCancelReadyPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundPlayedPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ServerBoundAbortPacket packet) {
        onUnhandledPacket(packet);
    }

    protected void onUnhandledPacket(ServerBoundPacket packet) {
        // Default behavior: ignore
    }
}
