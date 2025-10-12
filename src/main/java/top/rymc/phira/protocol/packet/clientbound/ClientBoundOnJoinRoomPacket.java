package top.rymc.phira.protocol.packet.clientbound;


import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.data.UserProfile;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;


@RequiredArgsConstructor
public class ClientBoundOnJoinRoomPacket extends ClientBoundPacket {

    private final UserProfile userProfile;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, userProfile);
    }
}
