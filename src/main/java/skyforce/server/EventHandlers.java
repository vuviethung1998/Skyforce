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
        System.out.printf("[SERVER] receive PlayerActionPacket: %s\n", connection.getPlayerName());
        UpdateGamePacket update = new UpdateGamePacket(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(update);
        }
    }

    private static void handleStartGameRequest(StartGameRequestPacket p, Connection connection) {
        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(new StartGameResponsePacket());
        }
    }
}
