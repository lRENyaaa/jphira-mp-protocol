package top.rymc.phira.protocol.packet.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.handler.server.ServerBoundPacketHandler;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBoundTouchesPacket extends ServerBoundPacket {

    private final List<TouchFrame> frames;

    public static ServerBoundTouchesPacket create(List<TouchFrame> frames) {
        return new ServerBoundTouchesPacket(frames);
    }

    public static ServerBoundTouchesPacket create(List<TouchFrame> frames, byte[] trailer) {
        return create(frames).setTrailer(trailer, ServerBoundTouchesPacket.class);
    }

    public static ServerBoundTouchesPacket decode(ByteBuf buf) {
        return new ServerBoundTouchesPacket(
                NettyPacketUtil.decodeList(buf, TouchFrame::decode)
        ).setTrailer(buf, ServerBoundTouchesPacket.class);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, frames);
        PacketWriter.write(buf, trailer);
    }

    @Override
    public void handle(ServerBoundPacketHandler handler) {
        handler.handle(this);
    }
}
