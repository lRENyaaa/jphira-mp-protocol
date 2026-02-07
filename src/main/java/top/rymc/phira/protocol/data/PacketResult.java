package top.rymc.phira.protocol.data;

import io.netty.buffer.ByteBuf;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.Optional;
import java.util.function.Function;

public class PacketResult<T extends Encodeable> implements Encodeable {

    // Mimicking rust anyhow Result<T, String>
    // To accurately mimic the specific case of Result<(), String>, null result is permitted.
    private final String failedMessage;
    private final T result;

    // Must check success at first
    public boolean isSuccess() {
        return failedMessage == null;
    }

    public Optional<T> getResult() {
        if (failedMessage != null) {
            throw new IllegalStateException("Packet result is failed");
        }

        return Optional.ofNullable(result);
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
        return new PacketResult<>(result);
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


    public static <T extends Encodeable> PacketResult<T> decodeVoid(ByteBuf buf) {
        if (!buf.readBoolean()) {
            return new PacketResult<>(NettyPacketUtil.decodeString(buf));
        }

        return new PacketResult<>((T) null);
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
