package top.rymc.phira.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderException;
import top.rymc.phira.protocol.exception.BadVarintException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;

import java.nio.charset.StandardCharsets;

public class NettyPacketUtil {

    private static final int MAXIMUM_VARINT_SIZE = 5;

    public static int decodeVarInt(ByteBuf buf) {
        // Originally from Velocity VarInt reader
        // AI-generated Clean Room implementation

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

        throw new BadVarintException();
    }

    public static void encodeVarInt(ByteBuf buf, int value) {
        // Originally from https://github.com/astei/varint-writing-showdown/ under MIT License

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

    public static void writeString(ByteBuf buf, CharSequence str) {
        int size = ByteBufUtil.utf8Bytes(str);
        encodeVarInt(buf, size);
        buf.writeCharSequence(str, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buf, int maxLength) {
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

}
