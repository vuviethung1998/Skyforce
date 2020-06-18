package skyforce.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class Connection implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean running = false;

    private String playerName;
    private int id;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            running = true;

            while (running) {
                try {
                    Object data = in.readObject();
                    EventHandlers.received(data, this);
                } catch (ClassNotFoundException | SocketException e) {
                    close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!socket.isClosed()) {
            close();
        }
    }

    public void close() {
        System.out.printf("[SERVER] Client closed ClientID: %d\n", id);
        running = false;
        try {
            Server.connections.remove(id);
            if(GameManager.players != null){
                GameManager.players.remove(id);
            }
            EventHandlers.roomStatus.removeConnection(id);
            for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()){
                Connection c = entry.getValue();
                System.out.println("room status");
                System.out.println(EventHandlers.roomStatus);
                c.sendObject(EventHandlers.roomStatus);
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            out.reset();
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getId() {
        return this.id;
    }
}
