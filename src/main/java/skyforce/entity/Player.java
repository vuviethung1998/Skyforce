package skyforce.entity;


import skyforce.client.Client;
import skyforce.client.ui.ingamescreen.HelicopterImageLoader;
import skyforce.client.ui.ingamescreen.LoadImage;
import skyforce.common.Constants;
import skyforce.server.GameManager;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private int x;
    private int y;
    private int connectionId;
    private boolean fire;
    private boolean right;
    private boolean left;
    private int step;

    private long current;
    private long delay;
    private int health;
    private int score;

    public Player(int x, int connectionId, String name) {
        this.name = name;
        this.x = x;
        this.y = Constants.GAME_HEIGHT + Constants.PLAYER_WIDTH;
        this.connectionId = connectionId;
        this.current = System.nanoTime();
        this.step = 10;
        this.left = false;
        this.right = false;
        this.fire = false;
        this.delay = 100;
        this.health = 3;
        this.score = 0;
    }

    public void tick() {
        if (health > 0) {
            if (left) {
                if (x >= 50) {
                    x -= step;
                }
            }
            if (right) {
                if (x <= Constants.GAME_WIDTH + Constants.PLAYER_WIDTH/2) {
                    x += step;
                }
            }
            if (fire) {
                System.out.println("fire");
                long breaks = (System.nanoTime() - current) / 1000000;
                if (breaks > delay) {
                    GameManager.bullets.add(new Bullet(x + 11, y ));
                    current = System.nanoTime();
                }
            }
        }
    }

    public void setLeft(boolean bool){
        left = bool;
    }

    public void setRight(boolean bool){
        right = bool;
    }

    public void setFire(boolean bool){
        fire = bool;
    }

    public void render(Graphics g) {
        if (health > 0) {
            if (isMe()) {
                g.drawImage(HelicopterImageLoader.getPlaneFrame(), x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, null);
            } else {
                g.drawImage(LoadImage.player, x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, null);
            }
        }
    }

    private boolean isMe() {
        return connectionId == Client.getConnectionId();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getConnectionId() { return this.connectionId; }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return this.score;
    }

    public void incScore() {
        this.score = this.score + 1;
    }

    public String getName() {
        return this.name;
    }
}
