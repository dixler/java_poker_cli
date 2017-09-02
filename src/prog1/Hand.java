package prog1;

public class Hand {
	Card[] cards;
	private int num_cards;
	private int max_cards;
	private int score;
	

	public Hand(int num_cards) {
		max_cards = num_cards;
		cards = new Card[max_cards];
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
		cards[num_cards] = my_card;
		num_cards += 1;
		return;
	}
	public Card discard(int index) {
		Card discarded = cards[index];
		num_cards += -1;
		shift_left(index);

		return discarded;
	}
	private void shift_left(int index) {
		for(int i = index; i < num_cards-1; i++) {
			cards[i] = cards[i+1];
		}
		return;
	}
	public int find(char rank, char suit) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_suit() == suit && cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
	public Card peek(int index) {
		//System.out.printf("peeking %d\n", index);
		return cards[index];
	}
	
	
	/*
	 * 5 Card Draw specific methods
	 * Move to GamePlayer
	 * 
	 */
	private char[] suit_map = {	'C', 'D', 'S', 'H'};

	private char[] rank_map = {	'A', '2', '3', '4', 
								'5', '6', '7', '8', 
								'9', 'T', 'J', 'Q', 
								'K'};
	
	/*
	 * Return values
	 * 
	 * 
	 * 
	 * 
	 */
	public int eval_score() {
		score = get_high_card();
		if(is_straight() && is_flush()) {
			score = 7;
		}
		else if(is_straight()) {
			score = 3;
		}
		else if(is_flush()) {
			score = 4;
		}

		// handle rankings based on duplicates
		int ret_val = is_pair();
		switch(ret_val) {
		case 0:
			break;
		case 1:
			score = Math.max(0, score);
			break;
		case 2:
			score = Math.max(1, score);
			break;
		case 3:
			score = Math.max(2, score);
			break;
		case 4:
			score = Math.max(6, score);
			break;
		case 5:
			score = Math.max(5, score);
			break;
		}
		return score;
	}

	// there are faster searching algorithms for this
	private boolean is_straight() {
		int combo = 0;
		//System.out.printf("is_straight\n");
		for(int i = 0; i <= rank_map.length + 1; i++) {
			int count = count_by_rank(rank_map[i%rank_map.length]);
			//System.out.printf("%c: %d\n", rank_map[i%rank_map.length], count);
			if(count > 1) {
				return false;
			}
			else if(count == 0) {
				// we didn't find any matches for this card
				if(combo > 0) {
					// the next card in the sequence wasn't found
					if(i != 1)
						// CCCCCCOMBO_BREAKKER.wav
						return false;
					else
						// Ace was found, but not 2. It could still be a
						// royal flush so we reset it
						combo = 0;
				}
			}
			else if(count == 1) {
				combo += 1;
			}
			if(combo == max_cards) {
				break;
			}
		}
		return true;
	}
	private int get_high_card() {
		int count = 0;
		int index;
		for(index = rank_map.length - 1; index > 0; index += -1) {
			count = count_by_rank(rank_map[index]);
			if(count > 0)
				break;
		}
		return -1*rank_map.length + index;
	}
	private boolean is_flush() {
		for(int i = 0; i < suit_map.length; i++) {
			if(count_by_suit(suit_map[i]) == max_cards)
				return true;
		}
		return false;
	}
	/*
	 * more sane than writing the win priority of these
	 * return values:
	 * 		full_house = 5
	 * 		four_of_a_kind = 4
	 * 		three_of_a_kind = 3
	 * 		two_pair = 2
	 * 		one_pair = 1
	 * 		nothing = 0
	 */
	private int is_pair() {
		int score = 0;
		for(int i = 0; i < rank_map.length; i++) {
			int count = count_by_rank(rank_map[i]);
			if(count == 2) {
				if(score == 0)		// no score yet
					score = 1;		// 		thus ONE PAIR
				else if(score == 1)	// we already have a pair
					score = 2;		// 		thus TWO PAIR
				else if(score == 3)	// we already have 3 of a kind
					score = 5;		//		thus FULL HOUSE
			}
			else if(count == 3) {		
				if(score == 0)		// no score yet
					score = 3;		// 		thus THREE OF A KIND
				else if(score == 1)	// we already have a pair
					score = 5;		// 		thus FULL HOUSE
			}
			else if(count == 4) {		
				score = 4;		// 		thus THREE OF A KIND
			}
		}
		return score;
	}
	private int count_by_rank(char rank) {
		int count = 0;
		for(int i = 0; i < num_cards; i++) {
			if(cards[i].get_rank() == rank)
				count += 1;
		}
		return count;
	}
	private int count_by_suit(char suit) {
		int count = 0;
		for(int i = 0; i < num_cards; i++) {
			if(cards[i].get_suit() == suit)
				count += 1;
		}
		return count;
	}
}
