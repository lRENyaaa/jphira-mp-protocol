package top.rymc.phira.protocol.exception;

import io.netty.handler.codec.DecoderException;

public class NeedMoreDataException extends DecoderException {

    public NeedMoreDataException() {
        super("Buffer does not contain enough bytes to decode");
    }
}

