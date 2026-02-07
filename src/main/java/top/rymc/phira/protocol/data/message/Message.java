package top.rymc.phira.protocol.data.message;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import lombok.AccessLevel;
import lombok.Getter;
import top.rymc.phira.protocol.codec.Encodeable;
import top.rymc.phira.protocol.data.state.GameState;
import top.rymc.phira.protocol.util.PacketWriter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Message implements Encodeable {

    protected static final Map<Integer, Function<ByteBuf,? extends Message>> DECODER_MAP = Registry.getDecoderMap();
    protected static final Map<Class<? extends Message>,Integer> ENCODER_MAP = Registry.getEncoderMap();


    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.writeByte(buf, ENCODER_MAP.get(this.getClass()));
    }

    public static Message decode(ByteBuf buf) {
        int id = buf.readByte();
        Function<ByteBuf, ? extends Message> function = DECODER_MAP.get(id);
        if (function != null) {
            return function.apply(buf);
        }

        throw new DecoderException("Unknown Message id: " + id);
    }

    @Getter(AccessLevel.PRIVATE)
    private enum Registry {
        Chat(0x00, ChatMessage.class, ChatMessage::decode),
        CreateRoom(0x01, CreateRoomMessage.class, CreateRoomMessage::decode),
        JoinRoom(0x02, JoinRoomMessage.class, JoinRoomMessage::decode),
        LeaveRoom(0x03, LeaveRoomMessage.class, LeaveRoomMessage::decode),
        NewHost(0x04, NewHostMessage.class, NewHostMessage::decode),
        SelectChart(0x05, SelectChartMessage.class, SelectChartMessage::decode),
        GameStart(0x06, GameStartMessage.class, GameStartMessage::decode),
        Ready(0x07, ReadyMessage.class, ReadyMessage::decode),
        CancelReady(0x08, CancelReadyMessage.class, CancelReadyMessage::decode),
        CancelGame(0x09, CancelGameMessage.class, CancelGameMessage::decode),
        StartPlaying(0x0A, StartPlayingMessage.class, (buf) -> StartPlayingMessage.INSTANCE),
        Played(0x0B, PlayedMessage.class, PlayedMessage::decode),
        GameEnd(0x0C, GameEndMessage.class, (buf) -> GameEndMessage.INSTANCE),
        Abort(0x0D, AbortMessage.class, AbortMessage::decode),
        LockRoom(0x0E, LockRoomMessage.class, LockRoomMessage::decode),
        CycleRoom(0x0F, CycleRoomMessage.class, CycleRoomMessage::decode);

        private final int id;
        private final Class<? extends Message> clazz;
        private final Function<ByteBuf,? extends Message> decoder;

        <T extends Message> Registry(int id, Class<T> clazz, Function<ByteBuf,T> decoder) {
            this.id = id;
            this.clazz = clazz;
            this.decoder = decoder;
        }

        private static Map<Integer, Function<ByteBuf,? extends Message>> getDecoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    Registry::getId,
                    stateEnum -> buf -> stateEnum.getDecoder().apply(buf)
            )));
        }

        private static Map<Class<? extends Message>,Integer> getEncoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    Registry::getClazz,
                    Registry::getId
            )));
        }
    }
}
