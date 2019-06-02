package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import interfaces.UserInputInterface;

public class UI {

	private JFrame mainFrame;
	private JPanel tablePanel;
	private JPanel opponentPanel;
	private JPanel playerPanel;
	private UserInputInterface userInputInterface;
	private HashMap<Integer, JLabel> playerCardContainers = new HashMap<>();
	private HashMap<Integer, JLabel> opponentCardContainers = new HashMap<>();
	private JPanel cardsPlayedOfOpponentPanel;
	private JPanel cardsPlayedOfPlayerPanel;
	private JPanel northContainer;
	private JPanel southContainer;

	private JButton playButton;
	private JButton passButton;
	private JButton startButton;
	private String content;

	public UI(UserInputInterface userInputInterface) {
		super();
		this.userInputInterface = userInputInterface;
	}

	public void clearUI() {
		opponentPanel.removeAll();
		playerPanel.removeAll();
		playerCardContainers.clear();
		opponentCardContainers.clear();
		cardsPlayedOfPlayerPanel.removeAll();
		cardsPlayedOfOpponentPanel.removeAll();

	}

	public void initUI() {

		mainFrame = new JFrame("RunningFast");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(1500, 800));
		mainFrame.setPreferredSize(new Dimension(1500, 800));
		mainFrame.setLocation(50, 50);
		mainFrame.getContentPane().setBackground(Color.decode("#004d1a"));
		mainFrame.getContentPane().setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("rules");
		menu.setForeground(Color.GREEN);
		content = "failed to load rules";
		try {
			content = new String(Files.readAllBytes(Paths.get("rules.txt")), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				JOptionPane.showMessageDialog(mainFrame, content);
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		menuBar.setBackground(Color.black);
		menuBar.setOpaque(true);
		menuBar.add(menu);
		mainFrame.setJMenuBar(menuBar);

		northContainer = new JPanel();
		northContainer.setOpaque(false);
		northContainer.setLayout(new BorderLayout());
		southContainer = new JPanel();
		southContainer.setOpaque(false);
		southContainer.setLayout(new BorderLayout());

		JPanel playButtonContainer = new JPanel();
		playButtonContainer.setOpaque(false);
		playButtonContainer.setLayout(new FlowLayout());
		playButtonContainer.setPreferredSize(new Dimension(200, 30));
		playButton = new JButton("PLAY");
		playButton.setPreferredSize(new Dimension(400, 30));
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userInputInterface.onPlayPressed();
			}
		});
		passButton = new JButton("PASS");
		passButton.setPreferredSize(new Dimension(100, 30));
		passButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userInputInterface.onPassPressed();
			}
		});

		startButton = new JButton("START");
		startButton.setPreferredSize(new Dimension(100, 30));
		startButton.setBackground(Color.GREEN);
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("start pressed in UI");
				userInputInterface.onStartPressed();
			}
		});
		playButtonContainer.add(startButton);
		playButtonContainer.add(playButton);
		playButtonContainer.add(passButton);
		southContainer.add(playButtonContainer, BorderLayout.NORTH);

		opponentPanel = new JPanel();
		opponentPanel.setLayout(new GridBagLayout());
		opponentPanel.setMinimumSize(new Dimension(1000, 200));
		opponentPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 40, 0));
		opponentPanel.setOpaque(false);

		playerPanel = new JPanel();
		playerPanel.setLayout(new GridBagLayout());
		playerPanel.setMinimumSize(new Dimension(1000, 200));
		playerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 50, 0));
		playerPanel.setOpaque(false);

		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setOpaque(false);
		cardsPlayedOfOpponentPanel = new JPanel();
		cardsPlayedOfOpponentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));
		cardsPlayedOfOpponentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		cardsPlayedOfOpponentPanel.setOpaque(false);
		cardsPlayedOfPlayerPanel = new JPanel();
		cardsPlayedOfPlayerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
		cardsPlayedOfPlayerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		cardsPlayedOfPlayerPanel.setOpaque(false);
		tablePanel.add(cardsPlayedOfOpponentPanel, BorderLayout.NORTH);
		tablePanel.add(cardsPlayedOfPlayerPanel, BorderLayout.SOUTH);

		JPanel rightButtonPanel = new JPanel();
//		rightButtonPanel.setLayout(new BorderLayout());
		rightButtonPanel.setPreferredSize(new Dimension(80, 150));
		rightButtonPanel.setOpaque(false);

		JPanel leftButtonPanel = new JPanel();
//		leftButtonPanel.setLayout(new GridBagLayout());
		leftButtonPanel.setPreferredSize(new Dimension(80, 150));
		leftButtonPanel.setOpaque(false);

		setPlayButtonEnabled(false);
		setStartButtonEnabled(false);

		northContainer.add(opponentPanel, BorderLayout.CENTER);
		southContainer.add(playerPanel, BorderLayout.CENTER);

		mainFrame.getContentPane().add(northContainer, BorderLayout.NORTH);
		mainFrame.getContentPane().add(rightButtonPanel, BorderLayout.EAST);
		mainFrame.getContentPane().add(leftButtonPanel, BorderLayout.WEST);
		mainFrame.getContentPane().add(tablePanel, BorderLayout.CENTER);
		mainFrame.getContentPane().add(southContainer, BorderLayout.SOUTH);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void updateTable(boolean forPlayer, List<Integer> cardsBeingPlayed) {
		JPanel side = null;
		if (forPlayer) {
			side = cardsPlayedOfPlayerPanel;
		} else {
			side = cardsPlayedOfOpponentPanel;
		}
		for (int i = 0; i < cardsBeingPlayed.size(); i++) {
			int number = cardsBeingPlayed.get(i);
			Card card = new Card(number);
			JLabel label = new JLabel(new ImageIcon(card.getIconPath()));
			side.add(label);
		}
		JLabel gap = new JLabel(" ");
		side.add(gap);
		side.validate();
		side.repaint();
		mainFrame.validate();
	}

	public void playCards(boolean forPlayer, List<Integer> cardsBeingPlayed) { // 出牌
		System.out.println("play cards in ui");
		if (!forPlayer) {
			for (int i = 0; i < cardsBeingPlayed.size(); i++) {
				opponentPanel.remove(0);
			}
		} else {
			for (int j = 0; j < cardsBeingPlayed.size(); j++) {
				if (playerCardContainers.containsKey(cardsBeingPlayed.get(j))) {
					System.out.println("removing the card number " + cardsBeingPlayed.get(j) + " : "
							+ playerCardContainers.get(cardsBeingPlayed.get(j)));
					playerPanel.remove(playerCardContainers.get(cardsBeingPlayed.get(j)));
				} else {
					System.out.println("try to play a card not exist in UI!!!!!!");
				}

			}
		}
		playerPanel.repaint();
		playerPanel.validate();
		opponentPanel.repaint();
		opponentPanel.validate();
		mainFrame.repaint();
		mainFrame.validate();
	}

	public void dealCards(List<Integer> cardsOfPlayer) {// 发牌

		for (int i = 0; i < cardsOfPlayer.size(); i++) {
			JLabel label = new JLabel(new ImageIcon("icons/back.png"));
			label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			opponentCardContainers.put(i, label);
			opponentPanel.add(label);
		}

		for (int i = 0; i < cardsOfPlayer.size(); i++) {

			int number = cardsOfPlayer.get(i);
			Card card = new Card(number);
			JLabel label = new JLabel(new ImageIcon(card.getIconPath()));
			label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			label.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {

					if (label.getBorder() instanceof LineBorder) {
						label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
						label.repaint();
						userInputInterface.onCardClick(number, false);
					} else {
						label.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
						label.repaint();
						userInputInterface.onCardClick(number, true);
					}
					playerPanel.setOpaque(false);
					playerPanel.validate();
				}
			});

			playerCardContainers.put(number, label);
			playerPanel.add(label);

		}

		opponentPanel.validate();
		playerPanel.validate();
		northContainer.validate();
		southContainer.validate();
		mainFrame.validate();
	}

	public void setPlayButtonEnabled(boolean enabled) {
		playButton.setEnabled(enabled);
		passButton.setEnabled(enabled);
	}

	public void setStartButtonEnabled(boolean enabled) {
		startButton.setEnabled(enabled);
	}

	public void showResult(boolean win) {
		if (win) {
			JOptionPane.showMessageDialog(mainFrame, "you win!");
		} else {
			JOptionPane.showMessageDialog(mainFrame, "you lose!");
		}

	}

}
