package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.codec.Encodeable;

@RequiredArgsConstructor
public enum PacketResult implements Encodeable {
    SUCCESS(0x01),
    FAILED(0x00);

    private final int code;

    @Override
    public void encode(ByteBuf buf) {
        buf.writeByte(code);
    }
}
