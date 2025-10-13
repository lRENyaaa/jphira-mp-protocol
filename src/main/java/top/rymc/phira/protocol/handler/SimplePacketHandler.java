package top.rymc.phira.protocol.handler;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.packet.clientbound.ClientBoundPongPacket;
import top.rymc.phira.protocol.packet.serverbound.*;

@RequiredArgsConstructor
@Getter
public class SimplePacketHandler extends PacketHandler {

    private final Channel channel;

    @Override
    public void handle(ServerBoundPingPacket packet) {
        channel.writeAndFlush(ClientBoundPongPacket.INSTANCE);
    }

    @Override
    public void handle(ServerBoundAuthenticatePacket packet) {

    }

    @Override
    public void handle(ServerBoundChatPacket packet) {

    }

    @Override
    public void handle(ServerBoundTouchesPacket packet) {

    }

    @Override
    public void handle(ServerBoundJudgesPacket packet) {

    }

    @Override
    public void handle(ServerBoundCreateRoomPacket packet) {

    }

    @Override
    public void handle(ServerBoundJoinRoomPacket packet) {

    }

    @Override
    public void handle(ServerBoundLeaveRoomPacket packet) {

    }

    @Override
    public void handle(ServerBoundLockRoomPacket packet) {

    }

    @Override
    public void handle(ServerBoundCycleRoomPacket packet) {

    }

    @Override
    public void handle(ServerBoundSelectChartPacket packet) {

    }

    @Override
    public void handle(ServerBoundRequestStartPacket packet) {

    }

    @Override
    public void handle(ServerBoundReadyPacket packet) {

    }

    @Override
    public void handle(ServerBoundCancelReadyPacket packet) {

    }

    @Override
    public void handle(ServerBoundPlayedPacket packet) {

    }

    @Override
    public void handle(ServerBoundAbortPacket packet) {

    }
}
