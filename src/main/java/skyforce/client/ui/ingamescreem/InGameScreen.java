package skyforce.client.ui.ingamescreem;

import com.google.common.eventbus.Subscribe;
import skyforce.client.Client;
import skyforce.client.ui.ScreenManager;
import skyforce.common.Constants;
import skyforce.common.EventBuz;
import skyforce.packet.PlayerActionPacket;
import skyforce.packet.StartGameRequestPacket;
import skyforce.packet.StartGameResponsePacket;
import skyforce.packet.UpdateGamePacket;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;


public class InGameScreen extends JPanel implements ActionListener, KeyListener {
    private ArrayList<Player> players;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    private static Canvas canvas;

    public InGameScreen(int width, int height) {
        setSize(width, height);
        setVisible(true);

        players = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        EventBuz.getInstance().register(this);
        ScreenManager.getInstance().getWindow().addKeyListener(this);
        ScreenManager.getInstance().getWindow().setFocusable(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("okok");
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_SPACE:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.FIRE_PRESSED));
            case KeyEvent.VK_LEFT:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.LEFT_PRESSED));
            case KeyEvent.VK_RIGHT:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.RIGHT_PRESSED));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_SPACE:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.FIRE_RELEASED));
            case KeyEvent.VK_LEFT:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.LEFT_RELEASED));
            case KeyEvent.VK_RIGHT:
                Client.sendObject(new PlayerActionPacket(PlayerActionPacket.Action.RIGHT_RELEASED));
        }
    }

    @Subscribe
    public void onStartGame(StartGameResponsePacket e) {
        System.out.println("[CLIENT] onStartGame");
        renderCanvas();
        renderUI();
        renderUI();
    }


    @Subscribe
    public void onUpdateGame(UpdateGamePacket e) {
        System.out.println("[CLIENT] onUpdateGame");
        this.players.clear();
        this.bullets.clear();
        this.enemies.clear();

        this.players.addAll(e.players);
        this.bullets.addAll(e.bullets);
        this.enemies.addAll(e.enemies);
        renderUI();
    }

    private void renderCanvas() {
        canvas = new Canvas();
        canvas.setFocusable(false);
        canvas.setPreferredSize(new Dimension(Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT));
        add(canvas);
        canvas.setVisible(true);
        LoadImage.init();
    }

    private void renderUI() {
        BufferStrategy buffer = canvas.getBufferStrategy();
        if (buffer == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();
        g.clearRect(0,0,Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT);

        // draw
        g.drawImage(LoadImage.image, 50 ,50, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        for (Enemy e : enemies) {
            if (e.getX() >= 50 && e.getX() <= 450 - 25 && e.getY() <= 450 - 25 && e.getY() >= 50) {
                e.render(g);
            }
        }

        for (Player player : players) {
            player.render(g);
        }

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }

        g.setColor(Color.BLUE);
        // end of draw

        buffer.show();
        g.dispose();
    }

    private void exitGame() {
        EventBuz.getInstance().unregister(this);
    }

    @Override
    protected void finalize() throws Throwable {
        ScreenManager.getInstance().getWindow().removeKeyListener(this);
        super.finalize();
    }
}
