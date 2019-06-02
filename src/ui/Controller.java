package ui;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import interfaces.SocketInterface;
import interfaces.UserInputInterface;

public class Controller implements UserInputInterface, SocketInterface {

	private HashMap<Integer, Card> mSelectedCard = new HashMap<>();
	private static UI ui;
	private SocketServer socketServer;

	public void start() {

		ui = new UI(this);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ui.initUI();
			}
		});
		socketServer = new SocketServer(this);
		socketServer.run();

	}

	public void dealCards(List<Integer> cardNumberList) {
		sortCards(cardNumberList);
		if (EventQueue.isDispatchThread()) {
			ui.dealCards(cardNumberList);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ui.dealCards(cardNumberList);
				}
			});
		}

	}

	private void sortCards(List<Integer> cardNumberList) {
		Collections.sort(cardNumberList, new Comparator<Integer>() {
			@Override
			public int compare(Integer n1, Integer n2) {
				Card c1 = new Card(n1);
				Card c2 = new Card(n2);
				int diff = c1.getPoint() - c2.getPoint();
				if (diff != 0) {
					return diff;
				} else {
					return c1.getSuit() - c2.getSuit();
				}

			}
		});
	}

	public void updateTable(List<Integer> cardNumberList, boolean isMyUI) {
		Set<Integer> set = new HashSet<>(cardNumberList);
		cardNumberList.clear();
		cardNumberList.addAll(set);

		sortCards(cardNumberList);
		if (cardNumberList != null && cardNumberList.size() > 0) {
			if (EventQueue.isDispatchThread()) {
				ui.updateTable(isMyUI, cardNumberList);
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ui.updateTable(isMyUI, cardNumberList);
					}
				});
			}
		}

	}

	public void playCards(List<Integer> cardNumberList, boolean isMyUI) {
		sortCards(cardNumberList);
		if (cardNumberList != null && cardNumberList.size() > 0) {
			for (int i = 0; i < cardNumberList.size(); i++) {
				mSelectedCard.remove(cardNumberList.get(i));
			}
			if (EventQueue.isDispatchThread()) {
				ui.playCards(isMyUI, cardNumberList);
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ui.playCards(isMyUI, cardNumberList);
					}
				});
			}
		}

	}

	@Override
	public void onCardClick(int cardNumber, boolean selected) {
		if (selected) {
			if (mSelectedCard.get(cardNumber) == null) {
				mSelectedCard.put(cardNumber, new Card(cardNumber));
			}
		} else {
			if (mSelectedCard.get(cardNumber) != null) {
				mSelectedCard.remove(cardNumber);
			}
		}
	}

	private void executeCommand(String event) {
		Protocol protocol = new Protocol(event);
		switch (protocol.action) {
		case Protocol.READY:
			System.out.println("PCPlayer ready");
			ui.setStartButtonEnabled(true);
			break;
		case Protocol.DEAL_CARDS:
			dealCards(protocol.cardNumberList);
			socketServer.pushEvent("" + Protocol.OK);
			ui.setStartButtonEnabled(false);
			break;
		case Protocol.PLAY_CARDS:
			playCards(protocol.cardNumberList, true);
			updateTable(protocol.cardNumberList, true);
			socketServer.pushEvent("" + Protocol.OK);

			break;
		case Protocol.OPPONENT_PLAY_CARDS:
			playCards(protocol.cardNumberList, false);
			updateTable(protocol.cardNumberList, false);
			socketServer.pushEvent("" + Protocol.OK);
			break;
		case Protocol.ASKED_FOR_PLAY:
			System.out.println("PCPlayer is waiting for your play");
			ui.setPlayButtonEnabled(true);
			break;
		case Protocol.WIN:
			System.out.println("you win!");
			socketServer.pushEvent("" + Protocol.OK);
			ui.showResult(true);
			break;
		case Protocol.LOSE:
			System.out.println("you lose!");
			socketServer.pushEvent("" + Protocol.OK);
			ui.showResult(false);
			break;
		default:
			throw new IllegalArgumentException("wrong command!");
		}
	}

	@Override
	public void onMessageReceived(String event) {
		System.out.println("message received: " + event);
		executeCommand(event);
	}

	@Override
	public void onPlayPressed() {
		System.out.println("play pressed");
		if (mSelectedCard.size() > 0) {
			ui.setPlayButtonEnabled(false);
			String command = Protocol.getCommandFromActionAndList(Protocol.PLAY_CARDS,
					new ArrayList<>(mSelectedCard.keySet()));
			socketServer.pushEvent(command);
		} else {
			System.out.println("no card is selected");
		}
	}

	@Override
	public void onStartPressed() {
		System.out.println("start pressed");
		mSelectedCard.clear();
		ui.clearUI();
		socketServer.pushEvent("" + Protocol.START);
	}

	@Override
	public void onPassPressed() {
		System.out.println("pass pressed");
		ui.setPlayButtonEnabled(false);
		ArrayList<Integer> arrayList = new ArrayList<>();
		arrayList.add(0, 0); // number 0 for pass
		String command = Protocol.getCommandFromActionAndList(Protocol.PLAY_CARDS, arrayList);
		socketServer.pushEvent(command);
	}

}
