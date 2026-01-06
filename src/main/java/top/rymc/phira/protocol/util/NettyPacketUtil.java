package top.rymc.phira.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderException;
import top.rymc.phira.protocol.codec.Decodeable;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.exception.BadVarIntException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NettyPacketUtil {

    private NettyPacketUtil() {}

    private static final int MAXIMUM_VARINT_SIZE = 5;

    public static int decodeVarInt(ByteBuf buf) {
        // AI-generated implementation
        // Inspired by Velocity's VarInt optimization ideas, no GPL code was copied.
        // Velocity GitHub: https://github.com/PaperMC/Velocity

        if (!buf.isReadable()) {
            throw new NeedMoreDataException();
        }

        byte b = buf.readByte();
        if ((b & 0x80) == 0) {
            return b;
        }

        int value = b & 0x7F;
        int shift = 7;

        for (int i = 1; i < MAXIMUM_VARINT_SIZE; i++) {
            if (!buf.isReadable()) {
                throw new NeedMoreDataException();
            }

            b = buf.readByte();
            value |= (b & 0x7F) << shift;

            if ((b & 0x80) == 0) {
                return value;
            }

            shift += 7;
        }

        throw new BadVarIntException();
    }

    public static void encodeVarInt(ByteBuf buf, int value) {
        // https://github.com/astei/varint-writing-showdown/blob/6b1a4ba/src/jmh/java/me/steinborn/varintshowdown/res/BlendedVarIntWriter.java#L8-L26
        // MIT License | Copyright (c) 2021 Andrew Steinborn

        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buf.writeShort(w);
        } else if ((value & (0xFFFFFFFF << 21)) == 0) {
            int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
            buf.writeMedium(w);
        } else if ((value & (0xFFFFFFFF << 28)) == 0) {
            int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
            buf.writeInt(w);
        } else {
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16 | ((value >>> 14) & 0x7F | 0x80) << 8
                    | ((value >>> 21) & 0x7F | 0x80);
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }
    }

    public static void encodeString(ByteBuf buf, CharSequence str) {
        int size = ByteBufUtil.utf8Bytes(str);
        encodeVarInt(buf, size);
        buf.writeCharSequence(str, StandardCharsets.UTF_8);
    }

    public static String decodeString(ByteBuf buf, int maxLength) {
        int length = decodeVarInt(buf);

        if (length < 0 || length > maxLength) {
            throw new DecoderException(String.format("Bad string length: %s (max: %s)", length, maxLength));
        }

        if (!buf.isReadable(length)) {
            throw new NeedMoreDataException();
        }

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }


    public static float decodeFloat16LE(ByteBuf buf) {
        return Float16Util.halfToFloat(buf.readShortLE());
    }

    public static void encodeFloat16LE(ByteBuf buf, float value) {
        buf.writeShortLE(Float16Util.floatToHalf(value));
    }

    public static <T extends Decodeable> List<T> decodeList(ByteBuf buf, Supplier<T> constructor) {
        int length = decodeVarInt(buf);
        List<T> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            T decodeable = constructor.get();
            decodeable.decode(buf);
            list.add(decodeable);
        }
        return list;
    }

    public static <T extends Encodeable> void encodeList(ByteBuf buf, List<T> list) {
        int size = list.size();
        encodeVarInt(buf, size);
        for (T encodeable : list) {
            encodeable.encode(buf);
        }
    }


}
