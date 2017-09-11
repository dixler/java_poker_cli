public class Hand {
	public Card[] cards;
	private int num_cards;
	private int max_cards;
	
   /*
    * FUNCTION:   public Hand(int defined_max_cards)();
    * description:   constructor
    */
	public Hand(int defined_max_cards) {
		max_cards = defined_max_cards;
		cards = new Card[max_cards];
		num_cards = 0;
	}

   /*
    * FUNCTION:   public int get_num_cards();
    * description:   returns the current number of cards in the hand
    */
	public int get_num_cards() {
		return num_cards;
	}
   /*
    * FUNCTION:   public int get_max_cards();
    * description:   returns the maximum hand size
    */
	public int get_max_cards() {
		return max_cards;
	}
	
	
	/* 
	 * FUNCTION: int get_num_rank(int rank)
	 * description:	helper function in order_hand();
	 */
	private int get_num_rank(int rank) {
		int count = 0;
		for(int i = 0; i < this.num_cards; i++) {
			if(cards[i].get_rank() == rank)
				count += 1;
		}
		return count;
	}
      /*
       * FUNCTION:   void order_hand()
       * description:   was implemented last, simplification of other 
       * 				methods could yield better runtime(not bubble 
       * 				sorting could yield better runtime too)
       * 				EDIT:	DOUBLE BUBBLE!!!!!!!(well bubble sort 
       * 						is stable so it's alright I guess.
       */
	private void order_hand() {
		// yep, we're bubble sorting(sue me)
		int i = 0;
		while(i < get_num_cards()) {
			// let temp be the first element
			for(int j = i+1; j < get_num_cards(); j++) {
				// if there's a card greater than the card at card[i], swap card[j] and that card
				// gets the high card accidentally, but more importantly, puts the cards in an order
				// that sorting by occurrence will yield desired results
				if(this.cards[i].get_rank() < this.cards[j].get_rank()) {
					// swap
					Card temp = this.cards[j];
					this.cards[j] = this.cards[i];
					this.cards[i] = temp;
				}
			}
			i += 1;
		}
		// yep we're doing it again
		i = 0;
		while(i < get_num_cards()) {
			// let temp be the first element
			for(int j = i+1; j < get_num_cards(); j++) {
				// if there are more copies of a card lesser than temp, swap temp and that card
				if(get_num_rank(cards[i].get_rank()) < get_num_rank(cards[j].get_rank())) {
					// swap
					Card temp = this.cards[j];
					this.cards[j] = this.cards[i];
					this.cards[i] = temp;
				}
			}
			i += 1;
		}
		return;
	}
   /*
    * FUNCTION:   public void draw(Card my_card);
    * description:   draws a card to the hand
    */
	public void draw(Card my_card) {
		//System.out.printf("drawing: %c of %c\n", my_card.get_rank(), my_card.get_suit());
		cards[num_cards] = my_card;
		num_cards += 1;
		order_hand();
		return;
	}
   /*
    * FUNCTION:   public Card discard(int index);
    * description:   removes a card from the hand. removes gaps and empty space between elements.
    */
	public Card discard(int index) {
		if(index < 0 || index >= cards.length) {
			return null;
		}
		Card discarded = cards[index];
		num_cards += -1;
		shift_left(index);
		order_hand();
		return discarded;
	}
   /*
    * FUNCTION:   private void shift_left(int index);
    * description:   helper to remove gaps for discard();
    */
	private void shift_left(int index) {
		for(int i = index; i < num_cards; i++) {
			cards[i] = cards[i+1];
		}
		return;
	}
   /*
    * FUNCTION:   public int find_first_rank(int rank);
    * description:   get the index of the first card of rank: rank(by raw RANK (0, 1, 2, ... 12)
    */
	public int find_first_rank(int rank) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
   /*
    * FUNCTION:   public int find(int rank, int suit);
    * description:   get the index of the card with "rank" and "suit" returns -1 if not found
    */
	public int find(int rank, int suit) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_suit() == suit && cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
   /*
    * FUNCTION:   public void print(char[] rank_map, char[] suit_map);
    * description:   print the hand according to the rank_map and suit_map
    */
	public void print(char[] rank_map, char[] suit_map) {
		for(int i = 0; i < num_cards; i++) {
			System.out.printf("%c%c\n", rank_map[cards[i].get_rank()], suit_map[cards[i].get_suit()]);
		}
		return;
	}
   /*
    * FUNCTION:   public int get_rank(int index);
    * description:   given an index, return that card's rank
    */
	public int get_rank(int index) {
		return cards[index].get_rank();
	}
   /*
    * FUNCTION:   public int get_suit(int index);
    * description:   given an index, return that card's suit
    */
	public int get_suit(int index) {
		return cards[index].get_suit();
	}
   /*
    * FUNCTION:   public Card get_card(int index);
    * description:   given an index, return the Card at that index
    */
	public Card get_card(int index) {
		//System.out.printf("peeking %d\n", index);
		return cards[index];
	}
}
