package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.Objects;
import java.util.function.Function;

public class PacketResult<T extends Encodeable> implements Encodeable {

    private final String failedMessage;
    private final T result;

    // Must check success at first
    public boolean isSuccess() {
        return failedMessage == null;
    }

    public T getResult() {
        if (failedMessage != null) {
            throw new IllegalStateException("Packet result is failed");
        }

        return result;
    }

    public String getFailedMessage() {
        if (failedMessage == null) {
            throw new IllegalStateException("Packet result is success");
        }

        return failedMessage;
    }

    public static <T extends Encodeable> PacketResult<T> failed(String failedMessage) {
        return new PacketResult<>(failedMessage);
    }

    public static <T extends Encodeable> PacketResult<T> success(T result) {
        return new PacketResult<>(Objects.requireNonNull(result));
    }

    public static PacketResult<EncodeableVoid> successVoid() {
        return new PacketResult<>((EncodeableVoid) null);
    }

    private PacketResult(T result) {
        this.failedMessage = null;
        this.result = result;
    }

    private PacketResult(String failedMessage) {
        this.failedMessage = failedMessage;
        this.result = null;
    }

    public static <T extends Encodeable> PacketResult<T> decode(ByteBuf buf, Function<ByteBuf, T> decoder) {
        if (!buf.readBoolean()) {
            return new PacketResult<>(NettyPacketUtil.decodeString(buf));
        }

        return new PacketResult<>(decoder.apply(buf));
    }


    public static PacketResult<EncodeableVoid> decodeVoid(ByteBuf buf) {
        if (!buf.readBoolean()) {
            return new PacketResult<>(NettyPacketUtil.decodeString(buf));
        }

        return new PacketResult<>((EncodeableVoid) null);
    }

    @Override
    public void encode(ByteBuf buf) {

        boolean flag = failedMessage == null;
        buf.writeBoolean(flag);

        if (!flag) {
            NettyPacketUtil.encodeString(buf, failedMessage);
            return;
        }

        if (result != null) {
            result.encode(buf);
        }
    }
}
