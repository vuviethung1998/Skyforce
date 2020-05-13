package skyforce.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable{
    private int port;
    private boolean running;
    private ServerSocket server;

    public static HashMap<Integer, Connection> connections;

    public void start() {
        new Thread(this).start();
    }

    public Server(int port) {
        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connections = new HashMap<>();
    }


    @Override
    public void run() {
        running = true;
        System.out.println("[SERVER] Server started on port: " + port);

        while (running) {
            try {
                Socket socket = server.accept();
                initConnection(socket);
            } catch (EOFException e) {
                System.out.println("");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        shutdown();
    }

    private void initConnection(Socket socket) {
        int id = connections.size() + 1;
        Connection connection = new Connection(socket, id);
        Server.connections.put(id, connection);
        new Thread(connection).start();
    }

    public void shutdown() {
        running = false;

        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
