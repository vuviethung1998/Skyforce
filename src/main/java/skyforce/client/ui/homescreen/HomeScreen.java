package skyforce.client.ui.homescreen;

import skyforce.client.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static skyforce.config.Constants.*;


public class HomeScreen extends JPanel implements ActionListener{
    private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;
    private JLabel titleLb;

    public HomeScreen(int width, int height) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        createGameBtn = new JButton("Create Game");
        joinGameBtn = new JButton("Join Game");
        quitGameBtn = new JButton("Quit");

        titleLb = new JLabel("FuDuSkyWar", SwingConstants.CENTER);

        createGameBtn.setBounds(560, 320, 220, 50);
        createGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        joinGameBtn.setBounds(560, 396, 220, 50);
        joinGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        quitGameBtn.setBounds(560, 472, 220, 50);
        quitGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));

        titleLb.setBounds(475, 160, 390, 70);
        titleLb.setFont(new Font("Serif", Font.BOLD, 46));

        quitGameBtn.addActionListener(this);
        createGameBtn.addActionListener(this);
        joinGameBtn.addActionListener(this);

        add(createGameBtn);
        add(joinGameBtn);
        add(quitGameBtn);
        add(titleLb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitGameBtn) {
            System.exit(0);
        } else if (e.getSource() == createGameBtn) {
            createNewGame();
        } else if (e.getSource() == joinGameBtn) {
            System.out.println(2);
            ScreenManager.getInstance().navigate(WAITING_ROOM_SCREEN);
        }
    }

    private String enterPlayerName() {
        String name = JOptionPane.showInputDialog(
                this,
                "Enter player name:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE
        );
        return name;
    }

    private void createNewGame() {
        String playerName = enterPlayerName();
        if (playerName == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a nickname before starting game!"
            );
        } else if (playerName.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your nickname is too short(must be longer than 4)!"
            );
        } else if (playerName.length() > 16) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your nickname is too long(must be shorter than 16!"
            );
        }
    }
}