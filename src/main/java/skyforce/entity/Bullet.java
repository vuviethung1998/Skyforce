package skyforce.entity;

import skyforce.client.ui.ingamescreem.HelicopterImageLoader;
import skyforce.client.ui.ingamescreem.LoadImage;
import skyforce.common.Constants;

import java.awt.*;
import java.io.Serializable;

public class Bullet  implements Serializable {
    private int x;
    private int y;
    private int speed;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 10;
    }

    public void tick() {
        y = y - speed;
    }

    public void render(Graphics g) {
        if (y<50) return;
        g.drawImage(LoadImage.bullet, x, y, Constants.BULLET_WIDTH,Constants.BULLET_HEIGHT, null);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
