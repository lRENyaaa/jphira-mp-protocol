package top.rymc.phira.protocol.data.monitor.touch;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Decodeable;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.List;

@Getter
public class TouchFrame implements Encodeable, Decodeable {

    private float time;
    private List<TouchPoint> points;

    public TouchFrame() {
        // Empty constructor
    }

    public TouchFrame(float time, List<TouchPoint> points) {
        this.time = time;
        this.points = points;
    }

    @Override
    public void decode(ByteBuf buf) {
        time = buf.readFloatLE();
        points = NettyPacketUtil.decodeList(buf, TouchPoint::new);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, time);
        PacketWriter.write(buf, points);
    }
}
