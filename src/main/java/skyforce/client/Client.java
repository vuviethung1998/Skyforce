package skyforce.client;

import skyforce.common.Constants;
import skyforce.packet.JoinRoomRequestPacket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


public class Client implements Runnable{
    public static Client client;

    private String host;
    private int port;

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    private static boolean running = false;

    private static int connectionId;
    private static String ingameBackground;

    public Client(String host, int port, String playerName) {
        this.host = host;
        this.port = port;
        Client.ingameBackground = Constants.INGAME_BACKGROUND_GALAXY;
    }

    public static void connect(String host, int port, String playerName) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            client = new Client(host, port, playerName);
            new Thread(client).start();
            sendObject(new JoinRoomRequestPacket(playerName));
        } catch (IOException e) {
            System.out.println("[CLIENT] Unable to connect to the server");
            e.printStackTrace();
        }
    }

    public static void sendObject(Object packet) {
        try {
            out.writeObject(packet);
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
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                    close();
                } catch (EOFException e) {
                    close();
                    e.printStackTrace();
                    System.out.println("[CLIENT] Disconnected from server!");
                }
            }
            close();
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


    public static int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int id){
        connectionId = id;
    }

    public static void setRunning(boolean run) {
        running = run;
    }

    public static String getIngameBackground() {
        return Client.ingameBackground;
    }

    public static void setIngameBackground(String background) {
        Client.ingameBackground = background;
    }
}
