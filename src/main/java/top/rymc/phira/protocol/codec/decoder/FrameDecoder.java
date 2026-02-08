package top.rymc.phira.protocol.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCountUtil;
import top.rymc.phira.protocol.exception.BadVarIntException;
import top.rymc.phira.protocol.exception.NeedMoreDataException;
import top.rymc.phira.protocol.util.NettyPacketUtil;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (!ctx.channel().isActive()) {
            in.clear();
            return;
        }

        int skip = in.forEachByte(ByteProcessor.FIND_NON_NUL);

        if (skip < 0) {
            in.clear();
            return;
        }

        in.readerIndex(skip);
        in.markReaderIndex();

        int length;
        try {
            length = NettyPacketUtil.decodeVarInt(in);
        } catch (NeedMoreDataException e) {
            in.resetReaderIndex();
            return;
        } catch (BadVarIntException e) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            return;
        }

        if (length < 0) {
            ctx.close();
            ReferenceCountUtil.safeRelease(in);
            throw new CorruptedFrameException("Bad packet length");
        }

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf frame = in.readRetainedSlice(length);
        out.add(frame);

    }

}


