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

    private static final Map<Integer, Function<ByteBuf,? extends ServerBoundPacket>> CLIENT_BOUND_PACKET_MAP = ServerBound.getPacketMap();
    private static final Map<Class<? extends ClientBoundPacket>,Integer> SERVER_BOUND_PACKET_MAP = ClientBound.getPacketMap();

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

        Function<ByteBuf, ? extends ServerBoundPacket> decoder = CLIENT_BOUND_PACKET_MAP.get(packetId);
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
            int packetId = getServerBoundPacketId(packet);

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

    private static final Map<Class<? extends ClientBoundPacket>, Integer> SERVER_BOUND_PACKET_CACHE = new ConcurrentHashMap<>();

    private static int getServerBoundPacketId(ClientBoundPacket packet) throws CodecException {
        Class<? extends ClientBoundPacket> clazz = packet.getClass();

        Integer id = SERVER_BOUND_PACKET_CACHE.get(clazz);
        if (id != null) return id;

        for (Map.Entry<Class<? extends ClientBoundPacket>, Integer> entry : SERVER_BOUND_PACKET_MAP.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) {
                id = entry.getValue();
                SERVER_BOUND_PACKET_CACHE.put(clazz, id);
                return id;
            }
        }

        throw new CodecException("Unknown ClientBound packet class: " + clazz);
    }


    @Getter
    public enum ServerBound {
        Ping(0x00, ServerBoundPingPacket.class, () -> ServerBoundPingPacket.INSTANCE),

        Authenticate(0x01, ServerBoundAuthenticatePacket.class, ServerBoundAuthenticatePacket::new),
        Chat(0x02, ServerBoundChatPacket.class, ServerBoundChatPacket::new),

        Touches(0x03, ServerBoundTouchesPacket.class, ServerBoundTouchesPacket::new),
        Judges(0x04, ServerBoundJudgesPacket.class, ServerBoundJudgesPacket::new),

        CreateRoom(0x05, ServerBoundCreateRoomPacket.class, ServerBoundCreateRoomPacket::new),
        JoinRoom(0x06, ServerBoundJoinRoomPacket.class, ServerBoundJoinRoomPacket::new),
        LeaveRoom(0x07, ServerBoundLeaveRoomPacket.class, ServerBoundLeaveRoomPacket::new),
        LockRoom(0x08, ServerBoundLockRoomPacket.class, ServerBoundLockRoomPacket::new),
        CycleRoom(0x09, ServerBoundCycleRoomPacket.class, ServerBoundCycleRoomPacket::new),

        SelectChart(0x0A, ServerBoundSelectChartPacket.class, ServerBoundSelectChartPacket::new),
        RequestStart(0x0B, ServerBoundRequestStartPacket.class, ServerBoundRequestStartPacket::new),
        Ready(0x0C, ServerBoundReadyPacket.class, ServerBoundReadyPacket::new),
        CancelReady(0x0D, ServerBoundCancelReadyPacket.class, ServerBoundCancelReadyPacket::new),
        Played(0x0E, ServerBoundPlayedPacket.class, ServerBoundPlayedPacket::new),
        Abort(0x0F, ServerBoundAbortPacket.class, ServerBoundAbortPacket::new),;

        private final int id;
        private final Class<? extends ServerBoundPacket> clazz;
        private final Supplier<? extends ServerBoundPacket> constructor;

        <T extends ServerBoundPacket> ServerBound(int id, Class<T> clazz, Supplier<T> constructor) {
            this.id = id;
            this.clazz = clazz;
            this.constructor = constructor;
        }

        private static Map<Integer, Function<ByteBuf,? extends ServerBoundPacket>> getPacketMap() {
            return Arrays.stream(values()).collect(Collectors.toMap(
                    ServerBound::getId,
                    packetEnum -> buf -> {
                        ServerBoundPacket packet = packetEnum.getConstructor().get();
                        packet.decode(buf);
                        return packet;
                    })
            );
        }
    }

    @Getter
    public enum ClientBound {
        Pong(0x00, ClientBoundPongPacket.class),

        Authenticate(0x01, ClientBoundAuthenticatePacket.class),
        Chat(0x02, ClientBoundChatPacket.class),

        Touches(0x03, ClientBoundTouchesPacket.class),
        Judges(0x04, ClientBoundJudgesPacket.class),

        Message(0x05, ClientBoundMessagePacket.class),
        ChangeState(0x06, ClientBoundChangeStatePacket.class),
        ChangeHost(0x07, ClientBoundChangeHostPacket.class),

        CreateRoom(0x08, ClientBoundCreateRoomPacket.class),
        JoinRoom(0x09, ClientBoundJoinRoomPacket.class),
        OnJoinRoom(0x0A, ClientBoundOnJoinRoomPacket.class),
        LeaveRoom(0x0B, ClientBoundLeaveRoomPacket.class),
        LockRoom(0x0C, ClientBoundLockRoomPacket.class),
        CycleRoom(0x0D, ClientBoundCycleRoomPacket.class),

        SelectChart(0x0E, ClientBoundSelectChartPacket.class),
        RequestStart(0x0F, ClientBoundRequestStartPacket.class),
        Ready(0x10, ClientBoundReadyPacket.class),
        CancelReady(0x11, ClientBoundCancelReadyPacket.class),
        Played(0x12, ClientBoundPlayedPacket.class),
        Abort(0x13, ClientBoundAbortPacket.class);

        private final int id;
        private final Class<? extends ClientBoundPacket> clazz;

        <T extends ClientBoundPacket> ClientBound(int id, Class<T> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        private static Map<Class<? extends ClientBoundPacket>,Integer> getPacketMap() {
            return Arrays.stream(values()).collect(Collectors.toMap(
                    ClientBound::getClazz,
                    ClientBound::getId
            ));
        }
    }
}
