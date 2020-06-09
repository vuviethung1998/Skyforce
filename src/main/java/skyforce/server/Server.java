package skyforce.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable{
    private int port;
    private boolean runServer;
    private static ServerSocket server;

    static HashMap<Integer, Connection> connections;
    static GameManager gameManager;
    public static String gameStatus;

    public static void start(int port) {
        if (server == null) {
            new Thread(new Server(port)).start();
        }
    }

    public Server(int port) {
        this.port = port;
        connections = new HashMap<>();
    }


    @Override
    public void run() {
        runServer = true;
        gameStatus = "waiting";
        gameManager = new GameManager();

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[SERVER] Server started on port: " + port);

        while (runServer) {
            if(gameStatus.equals("waiting")) {

                System.out.println("waiting");
                try {
                    Socket socket = server.accept();
                    initConnection(socket);
                    System.out.println("next");
                } catch (IOException e) {
                    e.printStackTrace();
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
