package prog1;

public class Card {
	
	private int rank;
	private int suit;

	public Card(int my_rank, int my_suit) {
		// TODO Auto-generated constructor stub
		rank = my_rank;
		suit = my_suit;
		return;
	}
	public boolean equals(Object other) {
		return (((Card)other).rank == this.rank && ((Card)other).suit == this.suit);
	}
	public int get_rank() {
		return rank;
	}
	public int get_suit() {
		return suit;
	}
	// TODO debug purposes
	//*
	public void set_rank(int new_rank) {
		rank = new_rank;
		return;
	}
	public void set_suit(int new_suit) {
		suit = new_suit;
		return;
	}
	//*/
}
