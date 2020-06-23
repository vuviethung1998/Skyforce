package skyforce.client.ui.waitingroomscreen;

import com.google.common.eventbus.Subscribe;


import skyforce.client.Client;
import skyforce.client.ui.ScreenManager;
import skyforce.common.EventBuz;
import skyforce.packet.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import static skyforce.common.Constants.*;

public class WaitingRoomScreenHost extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton exitBtn;
	JButton startGameBtn;
	JButton readyBtn;

	private ArrayList<JLabel> slots;
	private ArrayList<JLabel> readySlots;
	private ArrayList<JPanel> readyBoxes;
	private ArrayList<Integer> connections;
	private UpdateRoomPacket roomStatus;

	public WaitingRoomScreenHost(int width, int height) {
		this.slots = new ArrayList<>();
		this.readySlots = new ArrayList<>();
		this.readyBoxes = new ArrayList<>();
		this.connections = new ArrayList<>();
		this.roomStatus = new UpdateRoomPacket();
		setSize(width, height);
		setLayout(null);
		initUI();
		setVisible(true);
		EventBuz.getInstance().register(this);
	}

	private void initUI() {
		int[] slotLocations = { 20, 240, 460, 680 };
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
		JPanel ret = new JPanel();
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

	private JPanel createReadyButton() {
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
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startGameBtn) {
			for (Map.Entry<Integer, UpdateRoomPacket.PlayerStatus> entry : roomStatus.getConnectionHashMap()
					.entrySet()) {
				UpdateRoomPacket.PlayerStatus pStatus = entry.getValue();
				if (!pStatus.getIsReady()) {
					JOptionPane.showMessageDialog(null, "Can not start the game, somebody are not ready");
					return;
				}
			}
			Client.sendObject(new StartGameRequestPacket());
		} else if (e.getSource() == readyBtn) {
			if (e.getSource() == readyBtn) {
				System.out.println("Ready");
				for (Map.Entry<Integer, UpdateRoomPacket.PlayerStatus> entry : roomStatus.getConnectionHashMap()
						.entrySet()) {
					int connectionId = entry.getKey();
					System.out.println(connectionId);
					System.out.println(Client.getConnectionId());
					if (connectionId == Client.getConnectionId()) {
						UpdateRoomPacket.PlayerStatus pStatus = entry.getValue();
						Client.sendObject(new ReadyPacket(!pStatus.getIsReady()));
						this.readySlots.get(connectionId).setText("Ready");
						this.readyBoxes.get(connectionId).setBackground(Color.GREEN);
					}
				}
			}
		}
		if (e.getSource() == exitBtn) {
			Client.sendObject(new ExitRoomPacket());
			ScreenManager.getInstance().navigate(HOME_SCREEN);
		}
	}

	@Subscribe
	public void opUpdate(UpdateRoomPacket p) {
		roomStatus = p;
		for (int i = 0; i < 4; i++) {
			if (!p.getConnectionHashMap().containsKey(i)) {
				this.slots.get(i).setText("");
				this.readyBoxes.get(i).setBackground(null);
				this.readySlots.get(i).setText("");
			}
		}
		for (Map.Entry<Integer, UpdateRoomPacket.PlayerStatus> entry : p.getConnectionHashMap().entrySet()) {
			int connectionId = entry.getKey();
			UpdateRoomPacket.PlayerStatus pStatus = entry.getValue();
			this.slots.get(connectionId).setText(pStatus.getPlayerName());
			if (pStatus.getIsReady()) {
				this.readySlots.get(connectionId).setText("Ready");
				this.readyBoxes.get(connectionId).setBackground(Color.GREEN);
			} else {
				this.readySlots.get(connectionId).setText("");
				this.readyBoxes.get(connectionId).setBackground(Color.WHITE);
			}
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