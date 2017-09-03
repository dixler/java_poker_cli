package prog1;

public class GameBot extends GamePlayer{
	public GameBot(int id, int num_cards) {
		super(id, num_cards);
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
	private int bot_logic() {
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
	public int turn() {
		return bot_logic();
	}

}