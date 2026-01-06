package top.rymc.phira.protocol.exception;

import io.netty.handler.codec.CorruptedFrameException;

public class BadVarIntException extends CorruptedFrameException {
    public BadVarIntException() {
        super("Bad VarInt decoded");
    }
}
