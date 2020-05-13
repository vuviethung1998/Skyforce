package skyforce.server;

import skyforce.packet.JoinRoomRequestPacket;
import skyforce.packet.JoinRoomResponsePacket;
import skyforce.packet.UpdateRoomPacket;

import java.util.Map;

public class EventHandlers {
    public static void received(Object p, Connection connection) {
        if (p instanceof JoinRoomRequestPacket) {
            handleJoinRoomRequest((JoinRoomRequestPacket) p, connection);
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
}
