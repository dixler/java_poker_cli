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
			if(this.count_by_rank((i)%rank_map.length) == 1 && this.count_by_rank((i+1)%rank_map.length) == 0 && this.count_by_rank((i-1)%rank_map.length) == 0) {
				rank_discard = i;
			}
		}
		// get that lone card's index and return it for discarding
		for(int i = 0; i < this.get_hand_size(); i++) {
			if(this.my_hand.peek(i).get_rank() == rank_discard)
				return i;
		}
		return -1;
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
			if(this.my_hand.peek(i).get_suit() == suit_discard)
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
			if(this.my_hand.peek(i).get_rank() == rank_discard)
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
			//System.out.printf("player %d score: %d\n", this.get_player_id(), score);
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
				//System.out.printf("evaluating high card\n");
				// is high_card

				// 4 cards same suit?
				if(this.is_flush(4)) {
					id_discard = find_lone_suit();
					/*
					System.out.printf("%d has 4 FLUSH!!!!!!!!!!!!!!11\n", this.player_id());
					this.print_hand();
					System.out.printf("Words");
					//*/
					// discard non similar
				}
				
				// 4 cards in sequence?
				else if(this.is_straight(4)) {
					System.out.printf("%d has 4 STRAIGHT!!!!!!!!!!!!!!11\n", this.get_player_id());
					id_discard = find_isolated_card();
					/*
					System.out.printf("%d has 4 STRAIGHT!!!!!!!!!!!!!!11\n", this.player_id());
					this.print_hand();
					System.out.printf("Words");
					//*/
					// discard non similar
				}
				
				else {
					//System.out.printf("High Card!\n");
					int kept_card_index = 0;
					boolean has_ace = false;
					for(int i = 0; i < this.get_hand_size(); i++) {
						if(this.my_hand.peek(i).get_rank() == 0) {
							has_ace = true;
							kept_card_index = i;
							//System.out.printf("%d: Ace Exception\n", this.get_player_id());
						}
					}

					// we want to keep the highest card
					int kept_card_rank = score - 1;
					// handle ace exception
					// unless we have an Ace
					if(!has_ace) {
						for(int i = 0; i < this.my_hand.get_num_cards(); i++) {
							// find the high card
							if(this.my_hand.peek(i).get_rank() == kept_card_rank) {
								kept_card_index = i;
								break;
							}
						}
					}
					//this.print_hand();
					if(kept_card_index > 0) {
						// we want to discard cards before the high card
						id_discard = 0;
					}
					else {
						// ace is at the 0 index so we can remove index 1 
						// repeatedly since discarding shifts everything
						// over
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
