package top.rymc.phira.protocol.packet.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import top.rymc.phira.protocol.packet.ClientBoundPacket;
import top.rymc.phira.protocol.util.PacketWriter;

@RequiredArgsConstructor
public class ClientBoundJudgesPacket extends ClientBoundPacket {

    private final int id;
    private final byte[] judges;

    @Override
    public void encode(ByteBuf buf) {
        PacketWriter.write(buf, id);

        // Yeah, we took a shortcut here and did not parse the data packets according to the agreement.
        // We simply passed on the data sent by the client as is.
        // I know this may be risky, but considering that the client side is all written in Rust.
        // So we may not need to worry about it for the time being.
        buf.writeBytes(judges);
    }
}
