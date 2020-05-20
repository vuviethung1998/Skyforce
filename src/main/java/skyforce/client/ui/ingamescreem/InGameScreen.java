package skyforce.client.ui.ingamescreem;

import javax.swing.*;

import static skyforce.common.Constants.WINDOW_HEIGHT;
import static skyforce.common.Constants.WINDOW_WIDTH;

public class InGameScreen extends JPanel {

    public InGameScreen(int width, int height) {
        new GameSetup("Skyforce 1999", WINDOW_WIDTH, WINDOW_HEIGHT).start();
    }
}
