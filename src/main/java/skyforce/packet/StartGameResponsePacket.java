package skyforce.packet;

import java.io.Serializable;

public class StartGameResponsePacket implements Serializable {
    private boolean ok;

    public StartGameResponsePacket(boolean ok) {
        this.ok = ok;
    }

    public boolean getOk() {
        return ok;
    }
}
