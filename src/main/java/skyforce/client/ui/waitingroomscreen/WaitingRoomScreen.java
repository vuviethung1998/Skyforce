package skyforce.client.ui.waitingroomscreen;

import com.google.common.eventbus.Subscribe;
import skyforce.client.Client;
import skyforce.client.ui.ScreenManager;
import skyforce.common.EventBuz;
import skyforce.packet.ReadyPacket;
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
    JButton readyBtn;

    private ArrayList<JLabel> slots;
    private ArrayList<JLabel> readySlots;
    private ArrayList<JPanel> readyBoxes;
    private ArrayList<Integer> connections;

    public WaitingRoomScreen(int width, int height) {
        this.slots = new ArrayList<>();
        this.readySlots = new ArrayList<>();
        this.readyBoxes = new ArrayList<>();
        this.connections = new ArrayList<>();
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
        readyBtn = new JButton("Ready");
        JSeparator separator = new JSeparator();

        exitBtn.setBounds(20, 540, 220, 50);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        startGameBtn.setBounds(330, 540, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        readyBtn.setBounds(650, 540, 220, 50);
        readyBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);
        startGameBtn.addActionListener(this);
        readyBtn.addActionListener(this);

        for (int i = 0; i < slotLocations.length; i++) {
            JPanel slot = createPlayerSlot(slotLocations[i], 210);
            add(slot);
        }

        add(exitBtn);
        add(startGameBtn);
        add(readyBtn);
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
        this.connections.add(NULL_CONNECTION);
        return ret;
    }

    private JPanel createReadyButton(){
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setSize(150, 100);
        p.setBorder(new LineBorder(Color.BLACK));
        p.setBounds(0, 0, 200, 50);
        JLabel readySlot = new JLabel("", SwingConstants.CENTER);
        readySlot.setBounds(0, 0, 150, 50);
        this.readySlots.add(readySlot);
        JPanel box = new JPanel();
        box.setBorder(new LineBorder(Color.BLACK));
        box.setBounds(150, 0, 50, 50);
        this.readyBoxes.add(box);
        p.add(readySlot);
        p.add(box);
        return  p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameBtn) {
            Client.sendObject(new StartGameRequestPacket());
        }
        if (e.getSource() == readyBtn){
            for(int i = 0; i < readySlots.size(); i++){
                if(this.connections.get(i) == Client.getConnectionId()){
                    if(!this.readySlots.get(i).getText().equals("Ready")){
                        this.readySlots.get(i).setText("Ready");
                        this.readyBoxes.get(i).setBackground(Color.GREEN);
                    } else {
                        this.readySlots.get(i).setText("");
                        this.readyBoxes.get(i).setBackground(Color.WHITE);
                    }
                    Client.sendObject(new ReadyPacket(Client.getPlayerName()));
                }
            }
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
        if (p.getOk()) {
            ScreenManager.getInstance().navigate(INGAME_SCREEN);
            System.out.printf("[Client %d] start Game\n", Client.getConnectionId());
            return;
        }
        System.out.printf("[Client %d] can not start Game\n", Client.getConnectionId());
    }
}