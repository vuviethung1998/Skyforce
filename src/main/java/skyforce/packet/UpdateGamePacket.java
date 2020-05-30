package skyforce.packet;

import skyforce.entity.Bullet;
import skyforce.entity.Enemy;
import skyforce.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UpdateGamePacket implements Serializable {
    public HashMap<Integer, Player> players;
    public ArrayList<Enemy> enemies;
    public ArrayList<Bullet> bullets;

    public UpdateGamePacket(HashMap<Integer, Player> players, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        this.players = players;
        this.enemies = enemies;
        this.bullets = bullets;
    }
}
