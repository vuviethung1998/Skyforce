package skyforce.packet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateRoomPacket implements Serializable {
    private List<String> playerNames;

    public UpdateRoomPacket() {
        this.playerNames = new ArrayList<>();
    }

    public void add(String playerName) {
        this.playerNames.add(playerName);
    }

    public List<String> getPlayerNames() {
        return this.playerNames;
    }
}
