package prog1;

public class Card {
	
	private char rank;
	private char suit;

	public Card(char my_rank, char my_suit) {
		// TODO Auto-generated constructor stub
		rank = my_rank;
		suit = my_suit;
		return;
	}
	public boolean equals(Object other) {
		return (((Card)other).rank == this.rank && ((Card)other).suit == this.suit);
	}
	public char get_rank() {
		return rank;
	}
	public char get_suit() {
		return suit;
	}
	public void set_rank(char new_rank) {
		rank = new_rank;
		return;
	}
	public void set_suit(char new_suit) {
		suit = new_suit;
		return;
	}
}
