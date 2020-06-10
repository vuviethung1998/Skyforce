package skyforce.packet;

import java.io.Serializable;

public class ReadyPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;

    public ReadyPacket(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
