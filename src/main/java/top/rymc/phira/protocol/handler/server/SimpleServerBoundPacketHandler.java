package top.rymc.phira.protocol.handler.server;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.packet.clientbound.ClientBoundPongPacket;
import top.rymc.phira.protocol.packet.serverbound.*;

@RequiredArgsConstructor
@Getter
public class SimpleServerBoundPacketHandler extends ServerBoundPacketHandler {

    protected final Channel channel;

    @Override
    public void handle(ServerBoundPingPacket packet) {
        channel.writeAndFlush(ClientBoundPongPacket.INSTANCE);
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
