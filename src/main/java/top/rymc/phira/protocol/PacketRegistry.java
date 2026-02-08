package top.rymc.phira.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.packet.clientbound.*;
import top.rymc.phira.protocol.packet.serverbound.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PacketRegistry {

    @Getter
    public enum ServerBound {
        Ping(0x00, ServerBoundPingPacket.class, (buf) -> ServerBoundPingPacket.INSTANCE),

        Authenticate(0x01, ServerBoundAuthenticatePacket.class, ServerBoundAuthenticatePacket::decode),
        Chat(0x02, ServerBoundChatPacket.class, ServerBoundChatPacket::decode),

        Touches(0x03, ServerBoundTouchesPacket.class, ServerBoundTouchesPacket::decode),
        Judges(0x04, ServerBoundJudgesPacket.class, ServerBoundJudgesPacket::decode),

        CreateRoom(0x05, ServerBoundCreateRoomPacket.class, ServerBoundCreateRoomPacket::decode),
        JoinRoom(0x06, ServerBoundJoinRoomPacket.class, ServerBoundJoinRoomPacket::decode),
        LeaveRoom(0x07, ServerBoundLeaveRoomPacket.class, (buf) -> ServerBoundLeaveRoomPacket.INSTANCE),
        LockRoom(0x08, ServerBoundLockRoomPacket.class, ServerBoundLockRoomPacket::decode),
        CycleRoom(0x09, ServerBoundCycleRoomPacket.class, ServerBoundCycleRoomPacket::decode),

        SelectChart(0x0A, ServerBoundSelectChartPacket.class, ServerBoundSelectChartPacket::decode),
        RequestStart(0x0B, ServerBoundRequestStartPacket.class, (buf) -> ServerBoundRequestStartPacket.INSTANCE),
        Ready(0x0C, ServerBoundReadyPacket.class, (buf) -> ServerBoundReadyPacket.INSTANCE),
        CancelReady(0x0D, ServerBoundCancelReadyPacket.class, (buf) -> ServerBoundCancelReadyPacket.INSTANCE),
        Played(0x0E, ServerBoundPlayedPacket.class, ServerBoundPlayedPacket::decode),
        Abort(0x0F, ServerBoundAbortPacket.class, (buf) -> ServerBoundAbortPacket.INSTANCE),;

        private final int id;
        private final Class<? extends ServerBoundPacket> clazz;
        private final Function<ByteBuf,? extends ServerBoundPacket> decoder;

        <T extends ServerBoundPacket> ServerBound(int id, Class<T> clazz, Function<ByteBuf,T> decoder) {
            this.id = id;
            this.clazz = clazz;
            this.decoder = decoder;
        }

        private static Map<Integer, Function<ByteBuf,? extends ServerBoundPacket>> getDecoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    ServerBound::getId,
                    packetEnum -> buf -> packetEnum.getDecoder().apply(buf))
            ));
        }

        private static Map<Class<? extends ServerBoundPacket>,Integer> getEncoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    ServerBound::getClazz,
                    ServerBound::getId
            )));
        }

        private static final Map<Class<? extends ServerBoundPacket>,Integer> ENCODER_MAP = getEncoderMap();

        public static ByteBuf encode(ServerBoundPacket packet, Supplier<ByteBuf> bufConstructor) {

            ByteBuf buf = bufConstructor.get();
            try {
                Class<? extends ServerBoundPacket> clazz = packet.getClass();
                Integer id = ENCODER_MAP.get(clazz);
                if (id == null) {
                    throw new EncoderException("Unknown ServerBound packet class: " + clazz.getName());
                }

                buf.writeByte(id);
                packet.encode(buf);

                return buf.asReadOnly();
            } catch (Exception e) {
                ReferenceCountUtil.safeRelease(buf);
                throw e;
            }

        }

        private static final Map<Integer, Function<ByteBuf,? extends ServerBoundPacket>> DECODER_MAP = getDecoderMap();

        public static ServerBoundPacket decode(ByteBuf buf) {

            int packetId = buf.readUnsignedByte();

            Function<ByteBuf, ? extends ServerBoundPacket> decoder = DECODER_MAP.get(packetId);
            if (decoder == null) {
                throw new DecoderException("Unknown ServerBound packet id: " + packetId);
            }

            return decoder.apply(buf);
        }
    }

    @Getter
    public enum ClientBound {
        Pong(0x00, ClientBoundPongPacket.class, (buf) -> ClientBoundPongPacket.INSTANCE),

        Authenticate(0x01, ClientBoundAuthenticatePacket.class, ClientBoundAuthenticatePacket::decode),
        Chat(0x02, ClientBoundChatPacket.class, ClientBoundChatPacket::decode),

        Touches(0x03, ClientBoundTouchesPacket.class, ClientBoundTouchesPacket::decode),
        Judges(0x04, ClientBoundJudgesPacket.class, ClientBoundJudgesPacket::decode),

        Message(0x05, ClientBoundMessagePacket.class, ClientBoundMessagePacket::decode),
        ChangeState(0x06, ClientBoundChangeStatePacket.class, ClientBoundChangeStatePacket::decode),
        ChangeHost(0x07, ClientBoundChangeHostPacket.class, ClientBoundChangeHostPacket::decode),

        CreateRoom(0x08, ClientBoundCreateRoomPacket.class, ClientBoundCreateRoomPacket::decode),
        JoinRoom(0x09, ClientBoundJoinRoomPacket.class, ClientBoundJoinRoomPacket::decode),
        OnJoinRoom(0x0A, ClientBoundOnJoinRoomPacket.class, ClientBoundOnJoinRoomPacket::decode),
        LeaveRoom(0x0B, ClientBoundLeaveRoomPacket.class, ClientBoundLeaveRoomPacket::decode),
        LockRoom(0x0C, ClientBoundLockRoomPacket.class, ClientBoundLockRoomPacket::decode),
        CycleRoom(0x0D, ClientBoundCycleRoomPacket.class, ClientBoundCycleRoomPacket::decode),

        SelectChart(0x0E, ClientBoundSelectChartPacket.class, ClientBoundSelectChartPacket::decode),
        RequestStart(0x0F, ClientBoundRequestStartPacket.class, ClientBoundRequestStartPacket::decode),
        Ready(0x10, ClientBoundReadyPacket.class, ClientBoundReadyPacket::decode),
        CancelReady(0x11, ClientBoundCancelReadyPacket.class, ClientBoundCancelReadyPacket::decode),
        Played(0x12, ClientBoundPlayedPacket.class, ClientBoundPlayedPacket::decode),
        Abort(0x13, ClientBoundAbortPacket.class, ClientBoundAbortPacket::decode);

        private final int id;
        private final Class<? extends ClientBoundPacket> clazz;
        private final Function<ByteBuf,? extends ClientBoundPacket> decoder;

        <T extends ClientBoundPacket> ClientBound(int id, Class<T> clazz, Function<ByteBuf,T> decoder) {
            this.id = id;
            this.clazz = clazz;
            this.decoder = decoder;
        }

        private static Map<Integer, Function<ByteBuf,? extends ClientBoundPacket>> getDecoderMap() {
            return Map.copyOf(Arrays.stream(values()).collect(Collectors.toMap(
                    ClientBound::getId,
                    packetEnum -> buf -> packetEnum.getDecoder().apply(buf))
            ));
        }

        private static Map<Class<? extends ClientBoundPacket>,Integer> getEncoderMap() {
            return Arrays.stream(values()).collect(Collectors.toMap(
                    ClientBound::getClazz,
                    ClientBound::getId
            ));
        }

        private static final Map<Class<? extends ClientBoundPacket>,Integer> ENCODER_MAP = getEncoderMap();

        public static ByteBuf encode(ClientBoundPacket packet, Supplier<ByteBuf> bufConstructor) {

            ByteBuf buf = bufConstructor.get();
            try {
                Class<? extends ClientBoundPacket> clazz = packet.getClass();
                Integer id = ENCODER_MAP.get(clazz);
                if (id == null) {
                    throw new EncoderException("Unknown ClientBound packet class: " + clazz.getName());
                }

                buf.writeByte(id);
                packet.encode(buf);

                return buf.asReadOnly();
            } catch (Exception e) {
                ReferenceCountUtil.safeRelease(buf);
                throw e;
            }

        }

        private static final Map<Integer, Function<ByteBuf,? extends ClientBoundPacket>> DECODER_MAP = getDecoderMap();

        public static ClientBoundPacket decode(ByteBuf buf) {

            int packetId = buf.readUnsignedByte();

            Function<ByteBuf, ? extends ClientBoundPacket> decoder = DECODER_MAP.get(packetId);
            if (decoder == null) {
                throw new DecoderException("Unknown ClientBound packet id: " + packetId);
            }

            return decoder.apply(buf);
        }
    }
}
