package top.rymc.phira.protocol.data.message;

import top.rymc.phira.protocol.codec.Encodeable;

public abstract class Message implements Encodeable {

    public abstract int getMessageId();

}
