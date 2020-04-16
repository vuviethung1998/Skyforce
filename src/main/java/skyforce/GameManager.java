package skyforce;

import java.awt.*;
import java.util.ArrayList;

public class GameManager {
    private Player player;
    public static ArrayList<Bullet> bullets;

    public GameManager() {

    }

    public void init() {
        player = new Player(GameSetup.GAME_WIDTH/2 + 50, GameSetup.GAME_HEIGHT + 20);
        player.init();
        bullets = new ArrayList<>();
    }

    public void tick() {
        player.tick();
        for (Bullet bullet : bullets) {
            bullet.tick();
        }
    }

    public void render(Graphics g) {
        player.render(g);
        for (Bullet bullet : bullets) {
            bullet.render(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getY() <= 50) {
                bullets.remove(i);
                i--;
            }
        }
    }
}
