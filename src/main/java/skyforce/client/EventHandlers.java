package skyforce.client;

import skyforce.common.EventBuz;
import skyforce.packet.JoinRoomResponsePacket;
import skyforce.packet.UpdateRoomPacket;

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
    }

    private static void handleJoinRoomResponsePacket(JoinRoomResponsePacket p, Client c) {
        System.out.printf(
                "[CLIENT] received JoinRoomResponsePacket from server: %s - %d\n",
                p.getPlayerName(),
                p.getId()
        );
    }

    private static void handleUpdateRoomPacket(UpdateRoomPacket p, Client c) {
        EventBuz.getInstance().post(p);
    }
}
