package skyforce;

import skyforce.client.ui.ScreenManager;

public class Main {
    public static void main(String[] args) {
//        GameSetup game = new GameSetup("SkyForce Game", 500, 600);
//        game.start();
        ScreenManager.getInstance().display();
    }
}
