public class Card {
	
	private int rank;
	private int suit;

   /*
    * FUNCTION:   public Card(int my_rank, int my_suit);
    * description:   constructor: creates the Card with specified rank and suit
    */
	public Card(int my_rank, int my_suit) {
		// TODO Auto-generated constructor stub
		rank = my_rank;
		suit = my_suit;
		return;
	}
   /*
    * FUNCTION:   public boolean equals(Object other);
    * description:   allows us to do comparisons based on the object
    */
	public boolean equals(Object other) {
		return (((Card)other).rank == this.rank && ((Card)other).suit == this.suit);
	}

   /*
    * FUNCTION:   public int get_rank();
    * description:   returns the rank of this card
    */
	public int get_rank() {
		return rank;
	}
   /*
    * FUNCTION:   public int get_suit();
    * description:   returns the suit of this card
    */
	public int get_suit() {
		return suit;
	}
	// TODO debug purposes
	/*
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
