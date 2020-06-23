package skyforce.packet;

import skyforce.client.Client;

import java.io.Serializable;

public class PlayerActionPacket implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum  Action {
        LEFT_PRESSED,
        RIGHT_PRESSED,
        FIRE_PRESSED,
        LEFT_RELEASED,
        RIGHT_RELEASED,
        FIRE_RELEASED
    }

    public Action action;
    public int connectionId;

    public PlayerActionPacket(Action action) {
        this.action = action;
        this.connectionId = Client.getConnectionId();
    }
}
