package skyforce.entity;

import skyforce.client.ui.ingamescreem.LoadImage;
import skyforce.common.Constants;

import java.awt.*;
import java.io.Serializable;

public class Enemy implements Serializable {
    private int x;
    private int y;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
        y = y + 1;
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
