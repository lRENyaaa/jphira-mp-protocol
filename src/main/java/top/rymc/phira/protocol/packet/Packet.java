package top.rymc.phira.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.codec.Singletonizable;

import java.util.function.IntFunction;

public abstract class Packet implements Encodeable {

    @Getter
    protected final Trailer trailer = new Trailer();

    public boolean hasTrailer() {
        return trailer.isPresent();
    }

    protected <T extends Packet> T setTrailer(byte[] data, Class<T> clazz) {
        return castAfterCheck(clazz, () -> trailer.set(data));
    }

    protected <T extends Packet> T setTrailer(ByteBuf buf, Class<T> clazz) {
        return castAfterCheck(clazz, () -> trailer.readFrom(buf));
    }

    private <T extends Packet> T castAfterCheck(Class<T> clazz, Runnable operation) {
        if (this.getClass() != clazz) {
            throw new AssertionError("setTrailer called with mismatched class: expected " + clazz.getName() + " but got " + this.getClass().getName());
        }
        operation.run();
        return clazz.cast(this);
    }

    private static boolean isSingleton(Packet packet) {
        return packet instanceof Singletonizable s && s.isSingleton();
    }

    public final class Trailer implements Encodeable {

        private volatile byte[] data;

        private Trailer() {}

        private void set(byte[] data) {
            if (isSingleton(Packet.this)) {
                return;
            }
            if (this.data != null) {
                throw new IllegalStateException("Trailer already set");
            }
            if (data != null && data.length > 0) {
                this.data = data.clone();
            }
        }

        private void readFrom(ByteBuf buf) {
            if (isSingleton(Packet.this)) {
                return;
            }
            if (this.data != null) {
                throw new IllegalStateException("Trailer already set");
            }
            if (buf.isReadable()) {
                this.data = ByteBufUtil.getBytes(buf);
            }
        }

        @Override
        public void encode(ByteBuf buf) {
            if (data != null && data.length > 0) {
                buf.writeBytes(data);
            }
        }

        public boolean isPresent() {
            return data != null;
        }

        public byte[] getBytes() {
            return data == null ? null : data.clone();
        }

        public ByteBuf getAsByteBuf(IntFunction<ByteBuf> constructor) {
            if (data == null || data.length == 0) {
                return null;
            }

            ByteBuf buf = constructor.apply(data.length);

            if (buf != null) {
                buf.writeBytes(data);
            }

            return buf;
        }
    }
}
