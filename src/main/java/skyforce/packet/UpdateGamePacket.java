package skyforce.packet;

import skyforce.client.ui.ingamescreem.Bullet;
import skyforce.client.ui.ingamescreem.Enemy;
import skyforce.client.ui.ingamescreem.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateGamePacket implements Serializable {
    public ArrayList<Player> players;
    public ArrayList<Enemy> enemies;
    public ArrayList<Bullet> bullets;

    public UpdateGamePacket(ArrayList<Player> players, ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        this.players = players;
        this.enemies = enemies;
        this.bullets = bullets;
    }
}
