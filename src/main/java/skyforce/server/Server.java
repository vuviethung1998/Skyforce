package skyforce.server;

import skyforce.packet.UpdateGamePacket;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable{
    private int port;
    private boolean runServer;
    private ServerSocket server;

    public static HashMap<Integer, Connection> connections;
    public static GameManager gameManager;
    private static String gameStatus;

    private int fps;
    private double timePerTick;
    private double delta;
    private long current;

    public void start() {
        new Thread(this).start();
    }

    public Server(int port) {
        this.port = port;
        this.gameStatus = "waiting";

        this.fps = 30;
        this.timePerTick = 1000000000 / fps;
        this.delta = 0;
        this.current = System.nanoTime();

        this.gameManager = new GameManager();

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connections = new HashMap<>();
    }


    @Override
    public void run() {
        runServer = true;
        System.out.println("[SERVER] Server started on port: " + port);

        while (runServer) {
            if(gameStatus.equals("waiting")){
                System.out.println("waiting");
                try {
                    Socket socket = server.accept();
                    initConnection(socket);
                    System.out.println("next");
                } catch (EOFException e) {
                    System.out.println("");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(gameStatus.equals("running")) {
                System.out.println("running");
                delta = delta + (System.nanoTime() - current) / timePerTick;
                current = System.nanoTime();
                if (delta >= 1) {
                    UpdateGamePacket updateGamePacket = gameManager.tick();
                    for (Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
                        Connection c = entry.getValue();
                        c.sendObject(updateGamePacket);
                    }
                    delta--;
                }
            }
        }
        shutdown();
    }

    public static void setGameStatus(String status){
        gameStatus = status;
    }

    private void initConnection(Socket socket) {
        int id = connections.size() + 1;
        Connection connection = new Connection(socket, id);
        Server.connections.put(id, connection);
        new Thread(connection).start();
    }

    public void shutdown() {
        runServer = false;

        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
