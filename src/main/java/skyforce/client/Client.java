package skyforce.client;

import skyforce.packet.JoinRoomRequestPacket;
import skyforce.server.Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


public class Client implements Runnable{
    private String host;
    private int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean running = false;

    private int id;
    public String playerName;

    private boolean isServerDied = false;

    public Client(String host, int port, String playerName) {
        this.host = host;
        this.port = port;
        this.playerName = playerName;
        this.id = -1;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
            sendObject(new JoinRoomRequestPacket(playerName));

        } catch (IOException e) {
            System.out.println("[CLIENT] Unable to connect to the server");
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
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                    close();
                } catch (EOFException e) {
                    e.printStackTrace();
                    isServerDied = true;
                    close();
                    System.out.println("[CLIENT] Disconnected from server!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            running = false;
            if (socket != null) {
                if (!socket.isClosed()) {
                    in.close();
                    out.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
