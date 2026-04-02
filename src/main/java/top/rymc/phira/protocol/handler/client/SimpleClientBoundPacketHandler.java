package top.rymc.phira.protocol.handler.client;

import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.packet.clientbound.*;

public class SimpleClientBoundPacketHandler extends ClientBoundPacketHandler {

    @Override
    public void handle(ClientBoundPongPacket packet) {

    }

    @Override
    public void handle(ClientBoundAuthenticatePacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundChatPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundTouchesPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundJudgesPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundMessagePacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundChangeStatePacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundChangeHostPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundCreateRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundJoinRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundOnJoinRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundLeaveRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundLockRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundCycleRoomPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundSelectChartPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundRequestStartPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundReadyPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundCancelReadyPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundPlayedPacket packet) {
        onUnhandledPacket(packet);
    }

    @Override
    public void handle(ClientBoundAbortPacket packet) {
        onUnhandledPacket(packet);
    }

    protected void onUnhandledPacket(ClientBoundPacket packet) {
        // Default behavior: ignore
    }
}
