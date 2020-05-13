package skyforce.packet;

import java.io.Serializable;

public class JoinRoomRequestPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;

    public JoinRoomRequestPacket(String name) {
        this.playerName = name;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
