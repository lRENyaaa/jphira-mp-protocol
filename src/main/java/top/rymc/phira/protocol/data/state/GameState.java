package top.rymc.phira.protocol.data.state;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.util.collection.IntObjectMap;
import lombok.AccessLevel;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract sealed class GameState implements Encodeable permits Playing, WaitForReady, SelectChart {

    protected static final Map<Integer, Function<ByteBuf,? extends GameState>> DECODER_MAP = Registry.getDecoderMap();
    protected static final Map<Class<? extends GameState>,Integer> ENCODER_MAP = Registry.getEncoderMap();

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, ENCODER_MAP.get(this.getClass()));
    }

    public static GameState decode(ByteBuf buf) {
        int id = buf.readByte();
        Function<ByteBuf, ? extends GameState> function = DECODER_MAP.get(id);
        if (function != null) {
            return function.apply(buf);
        }

        throw new DecoderException("Unknown GameState id: " + id);
    }

    @Getter(AccessLevel.PRIVATE)
    private enum Registry {

        SelectChart(0x00, SelectChart.class,
                buf -> buf.readBoolean() ? new SelectChart(buf.readIntLE()) : new SelectChart()
        ),
        WaitForReady(0x01, WaitForReady.class, buf -> new WaitForReady()),
        Playing(0x02, Playing.class, buf -> new Playing());


        private final int id;
        private final Class<? extends GameState> clazz;
        private final Function<ByteBuf, ? extends GameState> decoder;

        <T extends GameState> Registry(int id, Class<T> clazz, Function<ByteBuf, T> decoder) {
            this.id = id;
            this.clazz = clazz;
            this.decoder = decoder;
        }

        private static Map<Integer, Function<ByteBuf,? extends GameState>> getDecoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    Registry::getId,
                    stateEnum -> buf -> stateEnum.getDecoder().apply(buf)
            )));
        }

        private static Map<Class<? extends GameState>,Integer> getEncoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    Registry::getClazz,
                    Registry::getId
            )));
        }
    }
}
