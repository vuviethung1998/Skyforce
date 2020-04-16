package skyforce;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
    private int x;
    private int y;
    private boolean fire;
    private boolean right;
    private boolean left;

    private long current;
    private long delay;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void init() {
        Display.frame.addKeyListener(this);
        current = System.nanoTime();
        delay = 100;
    }

    public void tick() {
        if (left) {
            if (x >= 50) {
                x = x - 4;
            }
        }
        if (right) {
            if (x <= 450 - 30) {
                x = x + 4;
            }
        }
        if (fire) {
            long breaks = (System.nanoTime() - current) / 1000000;
            if (breaks > delay) {
                GameManager.bullets.add(new Bullet(x + 15, y + 10));
                current = System.nanoTime();
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 30, 30);
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
}
