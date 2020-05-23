package skyforce.server;

import skyforce.entity.Bullet;
import skyforce.entity.Enemy;
import skyforce.entity.Player;
import skyforce.packet.PlayerActionPacket;
import skyforce.packet.UpdateGamePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameManager {
    public static ArrayList<Enemy> enemies;
    public static HashMap<Integer, Player> players;

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
            Random rand = new Random();
            players.put(connection.getId(), new Player(rand.nextInt(450), connection.getId()));
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
            System.out.println("gen enemy");
            for (int i = 0; i < 2; i++) {
                Random rand = new Random();
                int randX = rand.nextInt(450);
                int randY = rand.nextInt(450);
                enemies.add(new Enemy(randX, -randY));
            }
            current = System.nanoTime();
        }
    }

    private void removeCollisionEntities(){
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.getX() < 50 || e.getX() > 450 - 25 || e.getY() > 0) {
                enemies.remove(i--);
            }
        }

        for(Map.Entry<Integer, Player> entry: players.entrySet()){
            Player player = entry.getValue();

            player.tick();
            for(int i = 0; i < player.bullets.size(); i++) {
                Bullet bullet = player.bullets.get(i);
                bullet.tick();

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
                        player.bullets.remove(i--);
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

//    public void render(Graphics g) {
//        player.render(g);
//        for (Bullet bullet : bullets) {
//            bullet.render(g);
//        }

//        for (Enemy e : enemies) {
//            if (e.getX() >= 50 && e.getX() <= 450 - 25 && e.getY() <= 450 - 25 && e.getY() >= 50) {
//                e.render(g);
//            }
//        }

//        for (int i = 0; i < enemies.size(); i++) {
//            Enemy e = enemies.get(i);
//            if (isCollision(player, e)) {
//                enemies.remove(i);
//                i--;
//                player.setHealth(player.getHealth() - 1);
//                if (player.getHealth() <= 0) {
//                    System.out.println("Loss");
//                    enemies.clear();
//                }
//            }
//
//            for (int j = 0; j < bullets.size(); j++) {
//                Bullet b = bullets.get(j);
//                if (isCollision(e, b)) {
//                    enemies.remove(i);
//                    i--;
//                    bullets.remove(j);
//                    j--;
//                    player.incScore();
//                }
//            }
//        }

//        g.setColor(Color.BLUE);
//        g.drawString("Score: " + player.getScore(), 70, 500);
//    }

    private boolean isCollision(Enemy e, Bullet b) {
        return e.getX() < b.getX() + 6 &&
                e.getX() + 25 > b.getX() &&
                e.getY() < b.getY() + 6 &&
                e.getY() + 25 > b.getY();
    }

    private boolean isCollision(Player p, Enemy e) {
        return p.getX() < e.getX() + 25 &&
                p.getX() + 30 > e.getX() &&
                p.getY() < e.getY() + 25 &&
                p.getY() + 30 > e.getY();
    }
}
