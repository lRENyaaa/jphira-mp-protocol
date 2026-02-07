package top.rymc.phira.protocol.data.message;

public class GameEndMessage extends Message {

    public static GameEndMessage INSTANCE = new GameEndMessage();

    private GameEndMessage() {
        // Singleton instance
    }

}
