package skyforce.client.ui.waitingroomscreen;

import com.google.common.eventbus.Subscribe;
import skyforce.common.EventBuz;
import skyforce.packet.UpdateRoomPacket;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static skyforce.common.Constants.*;

public class WaitingRoomScreen extends JPanel implements ActionListener {

    private ArrayList<JLabel> slots;

    public WaitingRoomScreen(int width, int height) {
        this.slots = new ArrayList<>();
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
        EventBuz.getInstance().register(this);
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
            JPanel slot = createPlayerSlot(slotLocations[i], 210);
            add(slot);
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
        this.slots.add(name);
        return ret;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Subscribe
    public void opUpdate(UpdateRoomPacket p) {
        for (int i = 0; i < p.getPlayerNames().size(); i++) {
            String name = p.getPlayerNames().get(i);
            this.slots.get(i).setText(name);
        }
    }
}