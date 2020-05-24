package skyforce.server;

import skyforce.common.Constants;
import skyforce.entity.Bullet;
import skyforce.entity.Enemy;
import skyforce.entity.Player;
import skyforce.packet.PlayerActionPacket;
import skyforce.packet.UpdateGamePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager implements Runnable{
    private static ArrayList<Enemy> enemies;
    private static HashMap<Integer, Player> players;

    private long current;
    private long delay;

    public GameManager() {
        players = new HashMap<>();
        enemies = new ArrayList<>();

        delay = 800;
        current = System.nanoTime();
    }

    public void init(){
        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection connection = entry.getValue();
            int randX = ThreadLocalRandom.current().nextInt(Constants.PLAYER_WIDTH, Constants.GAME_WIDTH - Constants.PLAYER_WIDTH);
            players.put(connection.getId(), new Player(randX, connection.getId()));
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run(){
        System.out.println("running");

        double delta = 0;
        long current = System.nanoTime();
        int fps = 30;
        double timePerTick = 1000000000 / fps;

        while (true){
            delta = delta + (System.nanoTime() - current) / timePerTick;
            current = System.nanoTime();
            if (delta >= 1) {
                UpdateGamePacket updateGamePacket = Server.gameManager.tick();
                for (Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
                    Connection c = entry.getValue();
                    c.sendObject(updateGamePacket);
                }
                delta--;
            }
        }
    }

    public UpdateGamePacket tick() {
        this.generateEnemies();

        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).tick();
        }

        this.removeCollisionEntities();
        return new UpdateGamePacket(players, enemies);
    }

    private void generateEnemies(){
        long breaks = (System.nanoTime() - current)/1000000;
        if (breaks > delay) {
            int randX = ThreadLocalRandom.current().nextInt(Constants.ENEMY_WIDTH,Constants.GAME_WIDTH - Constants.ENEMY_WIDTH);
            enemies.add(new Enemy(randX, 0));
            current = System.nanoTime();
        }
    }

    private void removeCollisionEntities(){
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.getX() < Constants.ENEMY_WIDTH || e.getX() > Constants.GAME_WIDTH - Constants.ENEMY_WIDTH || e.getY() > Constants.GAME_HEIGHT) {
                enemies.remove(i--);
            }
        }

        for(Map.Entry<Integer, Player> entry: players.entrySet()){
            Player player = entry.getValue();

            player.tick();
            for(int i = 0; i < Player.bullets.size(); i++) {
                Bullet bullet = Player.bullets.get(i);
                bullet.tick();
                if (bullet.getY() < 0) {
                    Player.bullets.remove(i--);
                }

                for(int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (isCollision(player, enemy)) {
                        enemies.remove(j--);
                        player.setHealth(player.getHealth() - 1);
                        if (player.getHealth() <= 0) {
                            players.remove(player.getConnectionId());
                            System.out.println("Loss");
                        }
                    }

                    if (isCollision(enemy, bullet)) {
                        enemies.remove(j--);
                        Player.bullets.remove(i--);
                        player.incScore();
                    }
                }
            }
        }
    }

    public synchronized void handlePlayerAction(PlayerActionPacket packet){
        Player player = players.get(packet.connectionId);
        switch (packet.action){
            case LEFT_PRESSED:
                player.setLeft(true);
                break;
            case RIGHT_PRESSED:
                player.setRight(true);
                break;
            case FIRE_PRESSED:
                player.setFire(true);
                break;
            case LEFT_RELEASED:
                player.setLeft(false);
                break;
            case RIGHT_RELEASED:
                player.setRight(false);
                break;
            case FIRE_RELEASED:
                player.setFire(false);
                break;
            default:
                break;
        }
    }

    private boolean isCollision(Enemy e, Bullet b) {
        return e.getX() < b.getX() + 6 &&
                e.getX() + 25 > b.getX() &&
                e.getY() < b.getY() + 6 &&
                e.getY() + 25 > b.getY();
    }

    private boolean isCollision(Player p, Enemy e) {
        return p.getX() - Constants.PLAYER_WIDTH / 2 < e.getX() + Constants.ENEMY_WIDTH / 2 &&
                p.getX() + Constants.PLAYER_WIDTH / 2 > e.getX() - Constants.ENEMY_WIDTH / 2 &&
                p.getY() < e.getY() &&
                p.getY() < e.getY() + 25 &&
                p.getY() + 30 > e.getY();
    }
}
