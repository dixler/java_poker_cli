package prog1;

public class GamePlayer {
	Hand my_hand; //make private later
	private int player_id;

	public GamePlayer(int id, int num_cards) {
		my_hand = new Hand(num_cards);
		player_id = id;
		return;
	}
	public int player_id() {
		return player_id;
	}
	public void draw_card(Card dealt) {
		my_hand.draw(dealt);
		return;
	}
	public int hand_size() {
		return my_hand.get_num_cards();
	}
	private int ui() {
		int id_discard = -1;
		/*
		 * TODO interface
		 */
		// set id_discard to the card you want to discard
		return id_discard;
	}
	public Card discard(int index){
		return my_hand.discard(index);
	}
	public int turn() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return ui();
	}
	// TODO make more portable
	public boolean is_holding(char rank, char suit) {
		return my_hand.find(rank, suit) != -1;
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		// draw a card, print the card, then put it at the bottom
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			System.out.printf("%c of %c\n", rank_map[my_hand.peek(i).get_rank()], suit_map[my_hand.peek(i).get_suit()]);
		}
		return;
		//my_hand.print();
	}
	// TODO
	private void order_hand() {
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			// let temp be the first element
			Card temp = my_hand.peek(i);
			for(int j = i; j < my_hand.get_num_cards(); j++) {
				// if there's a card lesser than temp, swap temp and that card
			}
		}

		
		
	}
	/*
	 * 5 Card Draw specific methods
	 * Move to GamePlayer
	 * 
	 */
	protected char[] suit_map = {	'C', 'D', 'S', 'H'};

	protected char[] rank_map = {	'A', '2', '3', '4', 
									'5', '6', '7', '8', 
									'9', 'T', 'J', 'Q', 
									'K'};
	
	/*
	 * Return Values
	 * Straight Flush 7
	 * Straight 3
	 * Flush 4
	 * One Pair 0
	 * Two Pair 1
	 * Three of a Kind 2
	 * Four of a Kind 6
	 * Full House 5
	 * 
	 * High Card all else
	 */
	public int eval_score() {
		int score = get_high_card();
		if(is_straight(5) && is_flush(5)) {
			score = 7;
		}
		else if(is_straight(5)) {
			score = 3;
		}
		else if(is_flush(5)) {
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
	protected boolean is_straight(int target_straight) {
		int combo = 0;
		//System.out.printf("is_straight\n");
		for(int i = 0; i <= rank_map.length + 1; i++) {
			int count = count_by_rank(rank_map[i%rank_map.length]);
			//System.out.printf("%c: %d\n", rank_map[i%rank_map.length], count);
			if(count > 1) {
				// we have duplicates meaning there is no straight
				// NOTE: 	this could mess with AI mechanics, but 
				//			having 1 pair has higher priority than 
				//			straight so it's chill 
				//
				//			I'll put TODO here so I see it
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
			if(combo == target_straight){
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
	protected boolean is_flush(int target_flushed) {
		// do we have enough cards
		if(my_hand.get_num_cards() < target_flushed)
			// not enough cards to achieve desired flush
			return false;

		for(int i = 0; i < suit_map.length; i++) {
			if(count_by_suit(suit_map[i]) == target_flushed)
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
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(rank_map[my_hand.peek(i).get_rank()] == rank)
				count += 1;
		}
		return count;
	}
	private int count_by_suit(char suit) {
		int count = 0;
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(suit_map[my_hand.peek(i).get_suit()] == suit)
				count += 1;
		}
		return count;
	}

}
