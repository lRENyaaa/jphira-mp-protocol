package top.rymc.phira.protocol.exception;

import io.netty.handler.codec.CorruptedFrameException;

public class BadVarintException extends CorruptedFrameException {
    public BadVarintException() {
        super("Bad VarInt decoded");
    }
}
