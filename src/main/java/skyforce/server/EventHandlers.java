package skyforce.server;

import skyforce.packet.*;

import java.util.Map;

class EventHandlers {
    static UpdateRoomPacket roomStatus = new UpdateRoomPacket();

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

        if (p instanceof ReadyPacket){
            handleReadyPacket((ReadyPacket) p, connection);
            return;
        }

        if (p instanceof ExitRoomPacket){
            handleExitRoomPacket((ExitRoomPacket) p, connection);
            return;
        }
    }

    private static void handleJoinRoomRequest(JoinRoomRequestPacket p, Connection connection) {
        System.out.printf("[SERVER] receive JoinRoomRequestPacket: %s\n", p.getPlayerName());
        connection.setPlayerName(p.getPlayerName());
        connection.sendObject(new JoinRoomResponsePacket(connection.getPlayerName(), connection.getId()));

        roomStatus.addConnection(connection.getId(), p.getPlayerName(), false);

        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(roomStatus);
        }
    }

    private static void handlePlayerAction(PlayerActionPacket p, Connection connection) {
        System.out.printf("[SERVER] receive from [client: %d] PlayerActionPacket\n", connection.getId());
        Server.gameManager.handlePlayerAction(p);
    }

    private static void handleStartGameRequest(StartGameRequestPacket p, Connection connection) {
        System.out.printf("[SERVER] receive from [client: %d] StartGamePacket\n", connection.getId());

        if (Server.gameStatus.equals("running")) {
            for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
                Connection c = entry.getValue();
                if (c.getId() == connection.getId()) {
                    c.sendObject(new StartGameResponsePacket(false));
                }
            }
            return;
        }

        Server.setGameStatus("running");
        Server.gameManager.init();
        new Thread(Server.gameManager).start();

        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(new StartGameResponsePacket(true));
        }
    }

    private static void handleReadyPacket(ReadyPacket p, Connection connection){
        System.out.printf("[SERVER] receive from [client: %d] ReadyPacket\n", connection.getId());
        roomStatus.setIsReady(connection.getId(), p.isReady());
        for(Map.Entry<Integer, Connection> entry : Server.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(roomStatus);
        }
    }

    private static void handleExitRoomPacket(ExitRoomPacket p, Connection connection){
        connection.close();
    }
}
