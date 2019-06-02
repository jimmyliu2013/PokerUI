package ui;

import org.omg.CORBA.PRIVATE_MEMBER;

public class Card {

	public static final int SPADE = 1;
	public static final int HEART = 2;
	public static final int CLUB = 3;
	public static final int DIAMOND = 4;
	public static final int JOKER = 5;

	private int mCardNumber;
	private int mCardPoint;
	private int mCardSuit;

	public Card(int number) {
		if (number < 0 || number > 51) { // no joker
			throw new RuntimeException("invalid number, must be 0-53");
		}
		mCardNumber = number;

		if (number != 52 && number != 53) {
			mCardPoint = (number % 13);
			mCardSuit = number / 13 + 1;
		} else {
			mCardPoint = number;
			mCardSuit = Card.JOKER;
		}

	}

	public String getIconPath() {
		return "icons/" + mCardNumber + ".png";
	}

	public int getPoint() {
		return mCardPoint;
	}

	public int getSuit() {
		return mCardSuit;
	}

	public int getCardNumber() {
		return mCardNumber;
	}

	public String getSuitString() {
		String s;
		switch (mCardSuit) {
		case CLUB:
			s = "CLUB";
			break;
		case SPADE:
			s = "SPADE";
			break;
		case HEART:
			s = "HEART";
			break;
		case DIAMOND:
			s = "DIAMOND";
			break;
		case JOKER:
			s = "JOKER";
			break;

		default:
			throw new RuntimeException("get suit error");

		}
		return s;
	}

}
