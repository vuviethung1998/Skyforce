package skyforce.entity;


import skyforce.client.ui.ingamescreem.Display;
import skyforce.client.ui.ingamescreem.LoadImage;
import skyforce.common.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements KeyListener, Serializable {
    private int x;
    private int y;
    private int connectionId;
    public static ArrayList<Bullet> bullets;
    private boolean fire;
    private boolean right;
    private boolean left;
    private int step;

    private long current;
    private long delay;
    private int health;
    private int score;

    public Player(int x, int connectionId) {
        this.x = x;
        this.y = 450;
        this.connectionId = connectionId;
        bullets = new ArrayList<>();
        this.current = System.nanoTime();
        this.step = 10;
        this.left = false;
        this.right = false;
        this.fire = false;
        this.delay = 100;
        this.health = 3;
        this.score = 0;
    }

    public void init() {
        Display.frame.addKeyListener(this);
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
                    bullets.add(new Bullet(x + 11, y + 10));
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
            g.drawImage(LoadImage.player, x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, null);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (keycode == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (keycode == KeyEvent.VK_SPACE) {
            fire = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (keycode == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (keycode == KeyEvent.VK_SPACE) {
            fire = false;
        }
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
}
