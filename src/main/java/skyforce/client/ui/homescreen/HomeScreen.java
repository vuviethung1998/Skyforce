package skyforce.client.ui.homescreen;

import skyforce.client.Client;

import skyforce.client.ui.ScreenManager;
import skyforce.client.ui.ingamescreen.LoadImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static skyforce.common.Constants.*;


public class HomeScreen extends JPanel implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;

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

        createGameBtn.setBounds(560, 320, 220, 50);
        createGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        joinGameBtn.setBounds(560, 396, 220, 50);
        joinGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        quitGameBtn.setBounds(560, 472, 220, 50);
        quitGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));


        quitGameBtn.addActionListener(this);
        createGameBtn.addActionListener(this);
        joinGameBtn.addActionListener(this);

        add(createGameBtn);
        add(joinGameBtn);
        add(quitGameBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitGameBtn) {
            System.exit(0);
        } else if (e.getSource() == createGameBtn) {
            createNewGame();
        } else if (e.getSource() == joinGameBtn) {
            joinGame();
        }
    }

    private String[] enterPlayerName() {
        JTextField ip = new JTextField();
        JTextField port = new JTextField();
        JTextField name = new JTextField();
        Object[] message = {
                "ip:", ip,
                "player name:", name
        };
        int op = JOptionPane.showConfirmDialog(
                this,
                message,
                "Player Name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        return new String[]{name.getText(), ip.getText()};
    }

    private boolean validateName(String playerName) {
        if (playerName == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a nickname before starting game!"
            );
            return false;
        }

        if (playerName.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your nickname is too short(must be longer than 4)!"
            );
            return false;
        }

        if (playerName.length() > 16) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your nickname is too long(must be shorter than 16!"
            );
            return false;
        }
        return true;
    }

    private void createNewGame() {
        String[] inputs = enterPlayerName();
        if (!validateName(inputs[0])) {
            return;
        }

        ScreenManager.getInstance().navigate(WAITING_ROOM_SCREEN_HOST);
        Client.connect(inputs[1] == ""? "localhost": inputs[1], HOST_PORT, inputs[0]);
    }

    private void joinGame() {
        String[] inputs = enterPlayerName();
        if (!validateName(inputs[0])) {
            return;
        }

        ScreenManager.getInstance().navigate(WAITING_ROOM_SCREEN_PLAYER);
        Client.connect(inputs[1] == ""? "localhost": inputs[1],HOST_PORT, inputs[0]);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(LoadImage.imageLoader("/sky-force.jpg"), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }
}