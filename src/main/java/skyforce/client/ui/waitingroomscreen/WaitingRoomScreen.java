package skyforce.client.ui.waitingroomscreen;

import com.google.common.eventbus.Subscribe;
import skyforce.client.Client;
import skyforce.client.ui.ScreenManager;
import skyforce.common.EventBuz;
import skyforce.packet.StartGameRequestPacket;
import skyforce.packet.StartGameResponsePacket;
import skyforce.packet.UpdateRoomPacket;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static skyforce.common.Constants.*;

public class WaitingRoomScreen extends JPanel implements ActionListener {
    JButton exitBtn;
    JButton startGameBtn;

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
        exitBtn = new JButton("Exit Room");
        startGameBtn = new JButton("Start Game");
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
        String emptyPlayerName = "Free";
        JPanel ret  = new JPanel();
        ret.setLayout(null);
        ret.setSize(200, 200);
        ret.setBorder(new LineBorder(Color.BLACK));
        ret.setBounds(x, y, PLAYER_HOLDER_WIDTH, PLAYER_HOLDER_HEIGHT);

        JLabel name = new JLabel(emptyPlayerName, SwingConstants.CENTER);
        name.setBounds(20, 260, 160, 30);
        name.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        JPanel readyPanel = createReadyButton();

        ret.add(name);
        ret.add(readyPanel);
        this.slots.add(name);
        return ret;
    }

    private JPanel createReadyButton(){
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setSize(150, 100);
        p.setBorder(new LineBorder(Color.BLACK));
        p.setBounds(0, 0, 200, 50);
        JButton readyButton = new JButton("Ready");
        readyButton.setBounds(0, 0, 150, 50);
        JPanel box = new JPanel();
        box.setBorder(new LineBorder(Color.BLACK));
        box.setBounds(150, 0, 50, 50);
        ImageIcon icon = new ImageIcon("/success-tick.png");
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        box.add(thumb);
        p.add(readyButton);
        p.add(box);
        return  p;
    }

//    @Override
//    protected void paintComponent(Graphics graphics) {
//        super.paintComponent(graphics);
//        graphics.drawImage(new Image(), 0, 0, 50, 50,null);
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameBtn) {
            Client.sendObject(new StartGameRequestPacket());
        }
    }

    @Subscribe
    public void opUpdate(UpdateRoomPacket p) {
        for (int i = 0; i < p.getPlayerNames().size(); i++) {
            String name = p.getPlayerNames().get(i);
            this.slots.get(i).setText(name);
        }
    }

    @Subscribe
    public void onStartGame(StartGameResponsePacket p) {
        ScreenManager.getInstance().navigate(INGAME_SCREEN);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBuz.getInstance().unregister(this);
    }
}