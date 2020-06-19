package skyforce.entity;

import skyforce.client.ui.ingamescreen.LoadImage;
import skyforce.common.Constants;

import java.awt.*;
import java.io.Serializable;

public class Enemy implements Serializable {
    private int x;
    private int y;
    private int speed;

    public Enemy(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void tick() {
        y = y + speed;
    }

    public void render(Graphics g) {
        g.drawImage(LoadImage.enemy, x, y, Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
