package skyforce.client.ui.waitingroomscreen;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static skyforce.config.Constants.*;

public class WaitingRoomScreen extends JPanel implements ActionListener {


    public WaitingRoomScreen(int width, int height) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
    }


    private void initUI() {
        int[] slotLocations = {20, 240, 460, 680};
        JButton exitBtn = new JButton("Exit Room");
        JButton startGameBtn = new JButton("Start Game");
        JSeparator separator = new JSeparator();


        exitBtn.setBounds(20, 540, 220, 50);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        startGameBtn.setBounds(330, 540, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);
        startGameBtn.addActionListener(this);

        for (int i = 0; i < slotLocations.length; i++) {
            add(createPlayerSlot(slotLocations[i], 210));
        }

        add(exitBtn);
        add(startGameBtn);
        add(separator);
    }

    private JPanel createPlayerSlot(int x, int y) {
        JPanel ret  = new JPanel();
        ret.setLayout(null);
        ret.setSize(200, 200);
        ret.setBorder(new LineBorder(Color.BLACK));
        ret.setBounds(x, y, PLAYER_HOLDER_WIDTH, PLAYER_HOLDER_HEIGHT);

        JLabel name = new JLabel("Free", SwingConstants.CENTER);
        name.setBounds(20, 260, 160, 30);
        name.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));

        ret.add(name);
        return ret;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}