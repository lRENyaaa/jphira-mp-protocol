package top.rymc.phira.protocol.data.state;

import top.rymc.phira.protocol.codec.Encodeable;

public abstract sealed class GameState implements Encodeable permits Playing, WaitForReady, SelectChart {

}
