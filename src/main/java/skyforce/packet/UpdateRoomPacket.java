package skyforce.packet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateRoomPacket implements Serializable {
    private HashMap<Integer, PlayerStatus> connectionHashMap;

    public UpdateRoomPacket() {
        this.connectionHashMap = new HashMap<>();
    }

    public void addConnection(int connectionId, String playerName, boolean isReady){
        if(!connectionHashMap.containsKey(connectionId)){
            connectionHashMap.put(connectionId, new PlayerStatus(playerName, isReady));
        }
    }

    public HashMap<Integer, PlayerStatus> getConnectionHashMap(){
        return this.connectionHashMap;
    }

    public void setPlayerName(int connectionId, String playerName){
        PlayerStatus playerStatus = (PlayerStatus)connectionHashMap.get(connectionId);
        playerStatus.setPlayerName(playerName);
    }

    public void setIsReady(int connectionId, boolean isReady){
        PlayerStatus playerStatus = (PlayerStatus)connectionHashMap.get(connectionId);
        playerStatus.setIsReady(isReady);
    }

    public class PlayerStatus {
        private String playerName;
        private boolean isReady;

        public PlayerStatus(String name, boolean isReady){
            this.playerName = name;
            this.isReady = isReady;
        }

        public void setPlayerName(String name){
            this.playerName = name;
        }

        public void setIsReady(boolean isReady){
            this.isReady = isReady;
        }

        public String getPlayerName(){ return this.playerName;};
        public boolean getIsReady(){ return this.isReady;};
    }
}

