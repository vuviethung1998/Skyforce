package skyforce.packet;

import java.io.Serializable;

public class JoinRoomResponsePacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String playerName;

    public JoinRoomResponsePacket(String playerName, int id) {
        this.id = id;
        this.playerName = playerName;
    }

    public int getId() {
        return this.id;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
