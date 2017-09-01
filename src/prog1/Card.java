package prog1;

public class Card {
	
	private char _rank;
	private char _suit;

	public Card(char rank, char suit) {
		// TODO Auto-generated constructor stub
		_rank = rank;
		_suit = suit;
		return;
	}
	public char get_rank() {
		return _rank;
	}
	public char get_suit() {
		return _suit;
	}

}
