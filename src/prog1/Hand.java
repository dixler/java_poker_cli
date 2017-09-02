package prog1;

public class Hand {
	Card[] my_hand;
	private int num_cards;
	private int max_cards;
	private int score;

	public Hand(int num_cards) {
		max_cards = num_cards;
		my_hand = new Card[max_cards];
		num_cards = 0;
	}
	public int get_num_cards() {
		return num_cards;
	}
	public int get_max_cards() {
		return num_cards;
	}
	public void draw(Card my_card) {
		//System.out.printf("drawing: %c of %c\n", my_card.get_rank(), my_card.get_suit());
		my_hand[num_cards] = my_card;
		num_cards += 1;
		return;
	}
	public Card discard(int index) {
		Card discarded = my_hand[index];
		num_cards += -1;
		shift_left(index);

		return discarded;
	}
	private void shift_left(int index) {
		for(int i = index; i < num_cards-1; i++) {
			my_hand[i] = my_hand[i+1];
		}
		return;
	}
	public int find(char rank, char suit) {
		for(int i = 0; i < my_hand.length; i++) {
			if(my_hand[i].get_suit() == suit && my_hand[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
	public Card peek(int index) {
		//System.out.printf("peeking %d\n", index);
		return my_hand[index];
	}
	
	
	/*
	 * 5 Card Draw specific methods
	 * 
	 */
	private char[] suit_map = {	'C', 'D', 'S', 'H'};

	private char[] rank_map = {	'A', '2', '3', '4', 
								'5', '6', '7', '8', 
								'9', 'T', 'J', 'Q', 
								'K'};
	
	public int eval_score() {
		if(is_straight() && is_flush()) {
			score = 9;
		}
		return score;
	}

	private boolean is_straight() {
		return false;
	}
	private boolean is_flush() {
		for(int i = 0; i < suit_map.length; i++) {
			if(count_by_suit(suit_map[i]) == max_cards)
				return true;
		}
		return false;
	}
	/*
	 * return values:
	 * full_house = 5
	 * four_of_a_kind = 4
	 * three_of_a_kind = 3
	 * two_pair = 2
	 * one_pair = 1
	 * nothing = 0
	 */
	private int is_pair() {
		int score = 0;
		for(int i = 0; i < rank_map.length; i++) {
			int count = count_by_rank(rank_map[i]);
			if(count == 2)
				if(score == 0)		// no score yet
					score = 1;		// 		thus ONE PAIR
				else if(score == 1)	// we already have a pair
					score = 2;		// 		thus TWO PAIR
				else if(score == 3)	// we already have 3 of a kind
					score = 5;		//		thus FULL HOUSE

			if(count == 3) {		
				if(score == 0)		// no score yet
					score = 3;		// 		thus THREE OF A KIND
				else if(score == 1)	// we already have a pair
					score = 5;		// 		thus FULL HOUSE
			}
		}
		return 0;
	}
	private int count_by_rank(char rank) {
		int count = 0;
		for(int i = 0; i < num_cards; i++) {
			if(my_hand[i].get_rank() == rank)
				count += 1;
		}
		return count;
	}
	private int count_by_suit(char suit) {
		int count = 0;
		for(int i = 0; i < num_cards; i++) {
			if(my_hand[i].get_suit() == suit)
				count += 1;
		}
		return count;
	}
}
