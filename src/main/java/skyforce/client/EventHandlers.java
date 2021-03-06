package skyforce.client;

import skyforce.common.EventBuz;
import skyforce.packet.*;

public class EventHandlers {
    public static void received(Object p, Client client) {
        if (p instanceof JoinRoomResponsePacket) {
            handleJoinRoomResponsePacket((JoinRoomResponsePacket) p, client);
            return;
        }

        if (p instanceof UpdateRoomPacket) {
            handleUpdateRoomPacket((UpdateRoomPacket) p, client);
            return;
        }

        if (p instanceof UpdateGamePacket) {
            handleUpdateGamePacket((UpdateGamePacket) p, client);
            return;
        }

        if (p instanceof StartGameResponsePacket) {
            handleStartGameResponsePacket((StartGameResponsePacket) p, client);
            return;
        }

        if (p instanceof YouDiePacket) {
            EventBuz.getInstance().post(p);
            return;
        }
    }

    private static void handleJoinRoomResponsePacket(JoinRoomResponsePacket p, Client c) {
        System.out.printf(
                "[CLIENT: %d] received JoinRoomResponsePacket from server: %s\n",
                p.getId(),
                p.getPlayerName()
        );
        c.setConnectionId(p.getId());
    }

    private static void handleUpdateRoomPacket(UpdateRoomPacket p, Client c) {
        EventBuz.getInstance().post(p);
    }

    private static void handleUpdateGamePacket(UpdateGamePacket p, Client c) {
        EventBuz.getInstance().post(p);
    }

    private static void handleStartGameResponsePacket(StartGameResponsePacket p, Client c) {
        EventBuz.getInstance().post(p);
    }
}
