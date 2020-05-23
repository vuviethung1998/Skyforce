package skyforce.server;

import skyforce.packet.*;

import java.util.ArrayList;
import java.util.Map;

class EventHandlers {
    static void received(Object p, Connection connection) {
        if (p instanceof JoinRoomRequestPacket) {
            handleJoinRoomRequest((JoinRoomRequestPacket) p, connection);
            return;
        }

        if (p instanceof PlayerActionPacket) {
            handlePlayerAction((PlayerActionPacket) p, connection);
            return;
        }

        if (p instanceof StartGameRequestPacket) {
            handleStartGameRequest((StartGameRequestPacket) p, connection);
            return;
        }
    }

    private static void handleJoinRoomRequest(JoinRoomRequestPacket p, Connection connection) {
        System.out.printf("[SERVER] receive JoinRoomRequestPacket: %s\n", p.getPlayerName());
        connection.setPlayerName(p.getPlayerName());
        connection.sendObject(new JoinRoomResponsePacket(connection.getPlayerName(), connection.getId()));

        UpdateRoomPacket update = new UpdateRoomPacket();
        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            update.add(c.getPlayerName());
        }

        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(update);
        }
    }

    private static void handlePlayerAction(PlayerActionPacket p, Connection connection) {
        System.out.printf("[SERVER] receive from [client: %d] PlayerActionPacket\n", connection.getId());
        Server.gameManager.handlePlayerAction(p);

//        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
//            Connection c = entry.getValue();
//            c.sendObject(update);
//        }
    }

    private static void handleStartGameRequest(StartGameRequestPacket p, Connection connection) {
        System.out.printf("[SERVER] receive from [client: %d] StartGamePacket\n", connection.getId());
//        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
//            Connection c = entry.getValue();
//            c.sendObject(new StartGameResponsePacket());
//        }

        Server.gameManager.init();
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
}
