package prog1;

public class GameBot{
	protected Hand my_hand; //make private later
	private int player_id;
	protected Deck discard = new Deck();


	protected char[] suit_map = {	'C', 'D', 'S', 'H'};

	protected char[] rank_map = {	'2', '3', '4', '5', 
									'6', '7', '8', '9', 
									'T', 'J', 'Q', 'K', 
									'A'};
	public GameBot(int id, int num_cards) {
		my_hand = new Hand(num_cards);
		player_id = id;
		return;
	}
	public int get_player_id() {
		return player_id;
	}
	public void draw_card(Card dealt) {
		my_hand.draw(dealt);
		return;
	}
	public int get_hand_size() {
		return my_hand.get_num_cards();
	}
	public Card discard(int index){
		return my_hand.discard(index);
	}

	public void print_hand() {
		// draw a card, print the card, then put it at the bottom
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(my_hand.get_suit(i) % 2 == 0)
				//black
				;
			if(my_hand.get_suit(i) % 2 == 1)
				//red
				;
			System.out.printf("%c%c\n", rank_map[my_hand.get_rank(i)], suit_map[my_hand.get_suit(i)]);
		}
		return;
	}

	// TODO
	/*
	private void order_hand() {
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			// let temp be the first element
			Card temp = my_hand.get_card(i);
			for(int j = i; j < my_hand.get_num_cards(); j++) {
				// if there's a card lesser than temp, swap temp and that card
			}
		}
	}
	*/
	/*
	 * 5 Card Draw specific methods
	 * 
	 */
	
	/*
	 * Return Values
	 * ----------------------------------------------
	 * Straight Flush	21	|	Four of a Kind	20
	 * ----------------------------------------------
	 * Full House		19	|	Flush			18
	 * ----------------------------------------------
	 * Straight			17	|	Three of a Kind	16
	 * ----------------------------------------------
	 * Two Pair			15	|	One Pair		14
	 * ----------------------------------------------
	 * K				13	|	Q				12
	 * ----------------------------------------------
	 * J				11	|	T				10
	 * ----------------------------------------------
	 * 9				9	|	8				8
	 * ----------------------------------------------
	 * 7				7	|	6				6
	 * ----------------------------------------------
	 * 5				5 	|	4				4
	 * ----------------------------------------------
	 * 3				3	|	2				2
	 * ----------------------------------------------
	 * A				1	|
	 * ----------------------------------------------
	 */
	
	public int eval_score() {
		int[] pair_to_score = {14, 15, 16, 20, 19};
		int win_type = 1;
		if(is_straight(5) && is_flush(5)) {
			win_type = 21;
		}
		else if(is_straight(5)) {
			win_type = 17;
		}
		else if(is_flush(5)) {
			win_type = 18;
		}

		// handle rankings based on duplicates
		int pairness = is_pair();
		if(pairness > 0) {
			pairness = pair_to_score[pairness-1];
		}
		if(pairness > win_type) {
			win_type = pairness;
		}

		return win_type;
	}

	// there are faster searching algorithms for this
	private boolean holding_one_lesser(int rank) {
		if(this.count_by_rank((rank - 1 + rank_map.length) % rank_map.length) == 1) {
			return true;
		}
		return false;
	}
	protected boolean is_straight(int target_straight) {
		for(int i = 0; i < rank_map.length; i++) {
			// if it's a straight no duplicates
			if(this.count_by_rank(i) > my_hand.get_max_cards() - (target_straight - 1)) {
				return false;
			}
		}
		int num_adjacent = 0;
		for(int i = 0; i < rank_map.length + 1; i++) {
			// TODO:	count_by_rank(4) will break on the case of there being two of the same
			// 			rank, but since one pair will take priority over a near straight, it
			//			doesn't ever come down to this. Noting for clarity if there's ever a refactor
			if(count_by_rank(i) == 1 && holding_one_lesser(i)) {
				num_adjacent += 1;
			}
		}
		// TODO: Fix HARD EXCEPTION
		if(count_by_rank('K') == 1 && count_by_rank('2') == 1)
			return false;

		if((num_adjacent + 1) == target_straight) {
			return true;
		}
		return false;
	}
	/*
	private int get_high_card() {
		int count = 0;
		int rank_map_index;
		for(rank_map_index = rank_map.length - 1; rank_map_index > 0; rank_map_index += -1) {
			count = count_by_rank(rank_map[rank_map_index]);
			if(count > 0)
				break;
		}
		return rank_map_index + 1;
	}
	*/
	public int get_high_card() {
		boolean ace_high = true;
		int player_score = eval_score();
		if(player_score == 17 || player_score == 21) {
			// must have only 1
			if(count_by_rank('2') == 1) {
				// ace is low A 2 3 4 5
				ace_high = false;
			}
		}
		int max_count = 0;
		int high_card_rank = 0;
		for(int i = 0; i < rank_map.length; i += 1) {
			int challenger_count = count_by_rank(i);
			if(challenger_count >= max_count) {
				high_card_rank = i;
				max_count = challenger_count;
			}
		}
		if(!ace_high) {
			high_card_rank = 4; // '5'
		}
		return high_card_rank;
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
	public int count_by_rank(int rank) {
		int count = 0;
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(my_hand.get_rank(i) == rank)
				count += 1;
		}
		return count;
	}
	public int count_by_rank(char rank) {
		int count = 0;
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			// casts the integer value of the card's rank to a char 
			// and then checks it against the rank argument
			if(rank_map[my_hand.get_rank(i)] == rank)
				count += 1;
		}
		return count;
	}
	public int count_by_suit(char suit) {
		int count = 0;
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			// casts the integer value of the card's suit to a char 
			// and then checks it against the suit argument
			if(suit_map[my_hand.get_suit(i)] == suit)
				count += 1;
		}
		return count;
	}
	protected int count_by_suit(int suit) {
		int count = 0;
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(my_hand.get_suit(i) == suit)
				count += 1;
		}
		return count;
	}
	private int find_isolated_card() {
		int rank_discard = -1;
		// iterate through the hand and find anything with only one of it
		for(int i = 0; i < this.rank_map.length; i++) {
			// if theres a card at rank i and no cards before and after it, we need to discard this one
			// aces are tricky
			if(		this.count_by_rank((i)%rank_map.length) == 1	// there is a card of this rank
				&&	this.count_by_rank((i+1)%rank_map.length) == 0 	// there are no cards of one rank higher
				&&	this.count_by_rank((i-1)%rank_map.length) == 0) // there are no cards of one rank lower
			{
				rank_discard = i;
			}
		}
		// get that lone card's index and return it for discarding
		return my_hand.find_first_rank(rank_discard);
	}
	private int find_lone_suit() {
		int suit_discard = -1;
		// iterate through the hand and find anything with only one of it
		for(int i = 0; i < this.rank_map.length; i++) {
			if(this.count_by_suit(i) == 1) {
				suit_discard = i;
			}
		}
		// get that lone card's index and return it for discarding
		for(int i = 0; i < this.get_hand_size(); i++) {
			if(this.my_hand.get_suit(i) == suit_discard)
				return i;
		}
		return -1;
	}
	/*
	 * we return an index of an unpaired card(a card with no other cards of the same rank)
	 */
	private int find_unpaired() {
		int rank_discard = -1;
		// iterate through the hand and find anything with only one of it
		for(int i = 0; i < this.rank_map.length; i++) {
			if(this.count_by_rank(i) == 1) {
				rank_discard = i;
			}
		}
		// get that lone card's index and return it for discarding
		for(int i = 0; i < this.get_hand_size(); i++) {
			if(this.my_hand.get_rank(i) == rank_discard)
				return i;
		}
		return -1;
	}
	private int handle_one_pair() {
		return find_unpaired();
	}
	private int handle_two_pair() {
		return find_unpaired();
	}
	private int handle_three_of_a_kind() {
		return find_unpaired();
	}
	private int handle_four_of_a_kind() {
		return find_unpaired();
	}

	public Deck remove_unmatched(int max_swaps) {
		for(int i = 0; i < max_swaps; i++) {
			int id_discarded = bot_logic();
			if(bot_logic() != -1)
				discard.place_card(my_hand.get_card(id_discarded));
			else
				break;
		}
		return discard;
	}

	protected int bot_logic() {
		int id_discard = -1;
		int score = this.eval_score();
			switch(score) {
			case 21:
				// No work to be done STRAIGHT FLUSH
				break;
			case 20:
				id_discard = handle_four_of_a_kind();
				break;
			case 19:
				// No work to be done FULL HOUSE
				break;
			case 18:
				// No work to be done FLUSH
				break;
			case 17:
				// No work to be done STRAIGHT
				break;
			case 16:
				id_discard = handle_three_of_a_kind();
				break;
			case 15:
				id_discard = handle_two_pair();
				break;
			case 14:
				id_discard = handle_one_pair();
				break;
			default:
				// is high_card

				// 4 cards same suit?
				if(this.is_flush(4)) {
					// discard non similar
					id_discard = find_lone_suit();
				}
				
				// 4 cards in sequence?
				else if(this.is_straight(4)) {
					// discard non similar
					id_discard = find_isolated_card();
				}
				
				else {
					int kept_card_index = -1;
					boolean has_ace = false;
					// do we have an ace?
					kept_card_index = my_hand.find_first_rank(0);

					if(kept_card_index != -1) {
						has_ace = true;
					}

					// we want to keep the highest card
					// we can get the card's numerical 
					// rank from the score
					int kept_card_rank = score - 1;
					// handle ace exception
					// unless we have an Ace
					if(!has_ace) {
						kept_card_index = my_hand.find_first_rank(kept_card_rank);
					}
					if(kept_card_index > 0) {
						// we want to discard cards before the high card
						id_discard = 0;
					}
					else {
						// kept_card is at the 0 index so we can remove 
						// index 1 repeatedly since discarding shifts 
						// everything over
						id_discard = 1;
					}
					break;
				}
		}
		return id_discard;
	}
	public Deck turn(int max_swaps) {
		for(int i = 0; i < max_swaps; i++) {
			int id_discarded = bot_logic();
			if(bot_logic() != -1) {
				discard.place_card(my_hand.get_card(id_discarded));
				my_hand.discard(id_discarded);
			}
			else
				break;
		}
		return discard;
	}

}