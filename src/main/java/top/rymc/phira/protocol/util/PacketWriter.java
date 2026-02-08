package top.rymc.phira.protocol.util;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;

import java.util.List;

public final class PacketWriter {

    private PacketWriter() {}

    public static void write(ByteBuf buf, byte value) {
        buf.writeByte(value);
    }

    public static void writeByte(ByteBuf buf, int value) {
        buf.writeByte(value);
    }

    public static void write(ByteBuf buf, int value) {
        buf.writeIntLE(value);
    }

    public static void writeVarInt(ByteBuf buf, int value) {
        NettyPacketUtil.encodeVarInt(buf, value);
    }

    public static void write(ByteBuf buf, float value) {
        buf.writeFloatLE(value);
    }

    public static void writeFloat16(ByteBuf buf, float value) {
        NettyPacketUtil.encodeFloat16LE(buf, value);
    }

    public static void write(ByteBuf buf, boolean value) {
        buf.writeBoolean(value);
    }

    public static void write(ByteBuf buf, String string) {
        NettyPacketUtil.encodeString(buf, string);
    }

    public static void write(ByteBuf buf, Encodeable encodeable) {
        encodeable.encode(buf);
    }

    public static <T extends Encodeable> void write(ByteBuf buf, List<T> encodeables) {
        NettyPacketUtil.encodeList(buf, encodeables);
    }

}
