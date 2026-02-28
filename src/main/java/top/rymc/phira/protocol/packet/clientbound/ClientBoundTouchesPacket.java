package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.monitor.touch.TouchFrame;
import top.rymc.phira.protocol.handler.client.ClientBoundPacketHandler;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ClientBoundTouchesPacket extends ClientBoundPacket {

    private final int id;
    private final List<TouchFrame> frames;

    public static ClientBoundTouchesPacket create(int id, List<TouchFrame> frames) {
        return new ClientBoundTouchesPacket(id, frames);
    }

    public static ClientBoundTouchesPacket decode(ByteBuf buf) {
        return new ClientBoundTouchesPacket(
                buf.readIntLE(),
                NettyPacketUtil.decodeList(buf, TouchFrame::decode)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);
        PacketWriter.write(buf, frames);
    }

    @Override
    public void handle(ClientBoundPacketHandler handler) {
        handler.handle(this);
    }
}
