package skyforce.packet;

import java.io.Serializable;

public class ReadyPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean ready;

    public ReadyPacket(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return this.ready;
    }
}
