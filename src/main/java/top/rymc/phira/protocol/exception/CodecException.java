package top.rymc.phira.protocol.exception;

public class CodecException extends Exception {

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
