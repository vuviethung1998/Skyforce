package skyforce.client.ui;

import skyforce.client.ui.homescreen.HomeScreen;
import skyforce.client.ui.ingamescreem.InGameScreen;
import skyforce.client.ui.waitingroomscreen.WaitingRoomScreen;

import javax.swing.*;

import static skyforce.common.Constants.*;


public class ScreenManager {
    private static ScreenManager instance;
    private JFrame window;

    private ScreenManager() {
        window = new JFrame("Skyforce");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(false);

        navigate(HOME_SCREEN);
    }

    public void display() {
        window.setVisible(true);
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                if (null == instance) {
                    instance = new ScreenManager();
                }
            }
        }
        return instance;
    }

    public void navigate(String screenName) {
        window.getContentPane().removeAll();

        switch (screenName) {
            case HOME_SCREEN:
                window.getContentPane().add( new HomeScreen(SCREEN_WIDTH, SCREEN_HEIGHT));
                break;
            case WAITING_ROOM_SCREEN:
                window.getContentPane().add( new WaitingRoomScreen(SCREEN_WIDTH, SCREEN_HEIGHT));
                break;
            case INGAME_SCREEN:
                window.getContentPane().add( new InGameScreen(SCREEN_WIDTH, SCREEN_HEIGHT));
        }

        window.revalidate();
        window.repaint();
    }

    public JFrame getWindow() {
        return this.window;
    }
}
