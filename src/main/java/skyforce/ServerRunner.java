package skyforce;


import skyforce.common.Constants;
import skyforce.server.Server;

public class ServerRunner {
    public static void main(String[] args) {
        Server.start(Constants.HOST_PORT);
    }
}
