package top.rymc.phira.protocol.data.message;


public class StartPlayingMessage extends Message {

    public static StartPlayingMessage INSTANCE = new StartPlayingMessage();

    private StartPlayingMessage() {
        // Singleton instance
    }

}
