package top.rymc.phira.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.CorruptedFrameException;
import top.rymc.phira.protocol.exception.BadVarintException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;

import java.nio.charset.StandardCharsets;

public class NettyPacketUtil {

    private static final int MAXIMUM_VARINT_SIZE = 5;

    public static int readVarInt(ByteBuf buf) {
        int readable = buf.readableBytes();
        if (readable == 0) {
            throw new NeedMoreDataException();
        }

        int k = buf.readByte();
        if ((k & 0x80) != 128) {
            return k;
        }

        int i = k & 0x7F;
        for (int j = 1; j < MAXIMUM_VARINT_SIZE; j++) {
            if (!buf.isReadable()) {
                throw new NeedMoreDataException();
            }
            k = buf.readByte();
            i |= (k & 0x7F) << j * 7;
            if ((k & 0x80) != 128) {
                return i;
            }
        }
        throw new BadVarintException();
    }

    public static void writeVarInt(ByteBuf buf, int value) {
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buf.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buf.writeShort(w);
        } else {
            writeVarIntFull(buf, value);
        }
    }

    private static void writeVarIntFull(ByteBuf buf, int value) {
        // See https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/

        // This essentially is an unrolled version of the "traditional" VarInt encoding.
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
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
            buf.writeInt(w);
            buf.writeByte(value >>> 28);
        }
    }

    public static void writeString(ByteBuf buf, CharSequence str) {
        int size = ByteBufUtil.utf8Bytes(str);
        writeVarInt(buf, size);
        buf.writeCharSequence(str, StandardCharsets.UTF_8);
    }


    public static String readString(ByteBuf buf, int maxLength) {
        int length = readVarInt(buf);

        if (length < 0 || length > maxLength) {
            throw new CorruptedFrameException(String.format("Bad string length: %s (max: %s)", length, maxLength));
        }

        if (!buf.isReadable(length)) {
            throw new NeedMoreDataException();
        }

        String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
        buf.skipBytes(length);

        return string;
    }

}
