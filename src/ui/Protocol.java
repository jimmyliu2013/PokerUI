package ui;

import java.util.ArrayList;
import java.util.List;

public class Protocol {
	public int action;
	public List<Integer> cardNumberList;
	// protocol action field
	public static final int READY = 0;
	public static final int START = 1;
	public static final int DEAL_CARDS = 2;
	public static final int PLAY_CARDS = 3;
	public static final int OPPONENT_PLAY_CARDS = 4;
	public static final int OK = 5;
	public static final int ERROR = 6;
	public static final int ASKED_FOR_PLAY = 7;
	public static final int WIN = 8;
	public static final int LOSE = 9;

	public Protocol(String command) {
		parseCommandString(command);
	}

	private void parseCommandString(String command) {
		if (command == null) {
			throw new RuntimeException("event is null!");
		}
		String[] split = command.split(",");
		if (split.length == 0) {
			throw new IllegalArgumentException("empty message");
		}
		action = Integer.parseInt(split[0]);
		if (split.length > 1) {
			cardNumberList = new ArrayList<>();
			for (int i = 1; i < split.length; i++) {
				int number = Integer.parseInt(split[i]);
				if (number != 0) { // 0 for pass
					cardNumberList.add(number);
				}
			}
		}
	}

	public static String getCommandFromActionAndList(int action, List<Integer> cardNumberList) {
		StringBuilder builder = new StringBuilder();
		builder.append(action);
		if (cardNumberList != null) {

			for (int number : cardNumberList) {
				builder.append(",");
				builder.append(number);
			}
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("action: ");
		builder.append(action);
		if (cardNumberList != null) {
			builder.append(" ## cards: ");
			for (int number : cardNumberList) {
				builder.append(number);
			}
		}
		return builder.toString();
	}

}
