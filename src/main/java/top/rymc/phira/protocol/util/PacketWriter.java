package top.rymc.phira.protocol.util;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;

import java.util.List;

public class PacketWriter {

    private PacketWriter() {}

    public static void writeByte(ByteBuf buf, int value) {
        buf.writeByte(value);
    }

    public static void write(ByteBuf buf, int value) {
        buf.writeIntLE(value);
    }

    public static void write(ByteBuf buf, float value) {
        buf.writeFloatLE(value);
    }

    public static void write(ByteBuf buf, boolean value) {
        buf.writeBoolean(value);
    }

    public static void write(ByteBuf buf, String string) {
        NettyPacketUtil.writeString(buf, string);
    }

    public static void write(ByteBuf buf, Encodeable encodeable) {
        encodeable.encode(buf);
    }

    public static void write(ByteBuf buf, List<? extends Encodeable> encodeables) {
        writeByte(buf, encodeables.size());
        for (Encodeable encodeable : encodeables) {
            write(buf, encodeable);
        }
    }

}
