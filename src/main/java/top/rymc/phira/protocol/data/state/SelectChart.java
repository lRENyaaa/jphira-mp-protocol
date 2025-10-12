package top.rymc.phira.protocol.data.state;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public final class SelectChart extends GameState {

    private final Integer chartId;

    public SelectChart() {
        this(null);
    }

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, 0x00);
        boolean hasChartId = chartId != null;
        PacketWriter.write(buf, hasChartId);

        if (hasChartId) {
            PacketWriter.write(buf, chartId);
        }

    }

    @Override
    public String toString() {
        return "SelectChart{" +
                "chartId=" + chartId +
                '}';
    }
}
