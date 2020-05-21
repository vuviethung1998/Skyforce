package skyforce.packet;

import skyforce.entity.Enemy;
import skyforce.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateGamePacket implements Serializable {
    public HashMap<Integer, Player> players;
    public ArrayList<Enemy> enemies;

    public UpdateGamePacket(HashMap<Integer, Player> players, ArrayList<Enemy> enemies) {
        this.players = players;
        this.enemies = enemies;
    }
}
