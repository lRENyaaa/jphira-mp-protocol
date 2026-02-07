package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TouchFrame implements Encodeable {

    private final float time;
    private final List<TouchPoint> points;

    public static TouchFrame decode(ByteBuf buf) {
        return new TouchFrame(
                buf.readFloatLE(),
                NettyPacketUtil.decodeList(buf, TouchPoint::decode)
        );
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, time);
        PacketWriter.write(buf, points);
    }
}
