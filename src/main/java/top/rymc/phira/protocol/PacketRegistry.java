package top.rymc.phira.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import top.rymc.phira.protocol.codec.decoder.FrameDecoder;
import top.rymc.phira.protocol.exception.CodecException;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.packet.ServerBoundPacket;
import top.rymc.phira.protocol.packet.clientbound.*;
import top.rymc.phira.protocol.packet.serverbound.*;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PacketRegistry {

    private static final Map<Integer, Function<ByteBuf,? extends ServerBoundPacket>> SERVER_BOUND_PACKET_MAP = ServerBound.getDecoderMap();
    private static final Map<Class<? extends ClientBoundPacket>,Integer> CLIENT_BOUND_PACKET_MAP = ClientBound.getEncoderMap();

    /**
     * Decodes a client-bound packet from the given ByteBuf.
     *
     * <p>TCP is a streaming protocol and has no inherent packet boundaries.
     * The length VarInt at the start of each packet should be handled
     * by a ChannelHandler: it is responsible for assembling fragmented
     * data, merging multiple packets, and removing the length field.
     * Therefore, this method assumes 'buf' already contains only the
     * packet body (length field removed).</p>
     *
     * <p>If you are unsure how to handle packet boundaries and length fields,
     * refer to or use {@link FrameDecoder}.</p>
     *
     * @param buf the ByteBuf containing the packet body (length field removed)
     * @return the decoded ServerBoundPacket
     * @throws CodecException if the packet ID is unknown
     */
    public static ServerBoundPacket decode(ByteBuf buf) throws CodecException {

        int packetId = buf.readUnsignedByte();

        Function<ByteBuf, ? extends ServerBoundPacket> decoder = SERVER_BOUND_PACKET_MAP.get(packetId);
        if (decoder == null) {
            throw new CodecException("Unknown ServerBound packet id: " + packetId);
        }

        return decoder.apply(buf);
    }


    /**
     * Encodes a server-bound packet into a read-only {@link ByteBuf} ready for sending.
     *
     * @param packet the packet to encode
     * @param bufSupplier a supplier that provides a new writable {@link ByteBuf} for encoding
     * @return a read-only {@link ByteBuf} containing length + packet body
     * @throws CodecException if the packet class is unknown
     */
    public static ByteBuf encode(ClientBoundPacket packet, Supplier<ByteBuf> bufSupplier) throws CodecException {

        ByteBuf buf = bufSupplier.get();
        try {
            int packetId = getClientBoundPacketId(packet);

            buf.writeByte(packetId);
            packet.encode(buf);

            return buf.asReadOnly();
        } catch (Exception e) {
            ReferenceCountUtil.safeRelease(buf);
            throw e;
        }

    }

    /**
     * Encodes a server-bound packet into a read-only {@link ByteBuf} ready for sending.
     *
     * @param packet the packet to encode
     * @return a read-only {@link ByteBuf} containing length + packet body
     * @throws CodecException if the packet class is unknown
     */
    public static ByteBuf encode(ClientBoundPacket packet) throws CodecException {
        return encode(packet, Unpooled::buffer);
    }

    private static final Map<Class<? extends ClientBoundPacket>, Integer> CLIENT_BOUND_PACKET_CACHE = new ConcurrentHashMap<>();

    private static int getClientBoundPacketId(ClientBoundPacket packet) throws CodecException {
        Class<? extends ClientBoundPacket> clazz = packet.getClass();

        Integer id = CLIENT_BOUND_PACKET_CACHE.get(clazz);
        if (id != null) return id;

        for (Map.Entry<Class<? extends ClientBoundPacket>, Integer> entry : CLIENT_BOUND_PACKET_MAP.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                id = entry.getValue();
                CLIENT_BOUND_PACKET_CACHE.put(clazz, id);
                return id;
            }
        }

        throw new CodecException("Unknown ClientBound packet class: " + clazz);
    }


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
            return Arrays.stream(values()).collect(Collectors.toMap(
                    ServerBound::getId,
                    packetEnum -> buf -> packetEnum.getDecoder().apply(buf))
            );
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

        private static Map<Class<? extends ClientBoundPacket>,Integer> getEncoderMap() {
            return Arrays.stream(values()).collect(Collectors.toMap(
                    ClientBound::getClazz,
                    ClientBound::getId
            ));
        }
    }
}
