package prog1;

public class GameBot extends GamePlayer{
	public GameBot(int id, int num_cards) {
		super(id, num_cards);
	}
	
	
	/*
	private int handle_one_pair() {
		//System.out.printf("handling pair!\n");
		if(this.get_hand_size() == 2)
			// our hand is literally only two cards
			// normally, if we have a pair, we wouldn't be 
			// able to get here since we can only discard 3 
			// times, but whatever.
			return -1;
		int index_to_be_deleted = -1;
		for(int i = 0; i < this.get_hand_size(); i++) {
			int first_pair_card_rank = this.my_hand.peek(i).get_rank();
			for(int j = i+1; j < this.get_hand_size(); j++) {
				if(first_pair_card_rank == this.my_hand.peek(j).get_rank()) {
					// we know that the first card is one of the pairs and that
					// this card is the other pair
					if(index_to_be_deleted != -1)
						return index_to_be_deleted;
					else
						return j + 1;
				}
				// if we get here then j was not the chosen one!
				// i.e. j was not a copy of the first thing
				index_to_be_deleted = j;
			}
			// if we get here then i was not the chosen one!
			// i.e. i had no copies in the hand so we can mark it for removal
			index_to_be_deleted = i;
		}
		return -1;
	}
	*/
	private int handle_one_pair() {
		return handle_two_pair();
	}
	private int handle_two_pair() {
		// we go about finding the odd one out, by finding the only rank with count of 1
		// and then searching for it.... could've used this method for handle_one_pair, but
		// whatevs it works
		// was going to name my variable name third_wheel, but one of the slides in class said
		// variable names shouldn't be puns
		int rank_discard = -1;
		// iterate through 
		for(int i = 0; i < this.rank_map.length; i++) {
			if(this.count_by_rank(i) == 1) {
				rank_discard = i;
			}
		}
		for(int i = 0; i < this.get_hand_size(); i++) {
			if(this.my_hand.peek(i).get_rank() == rank_discard)
				return i;
		}
		return -1;
	}
	private int handle_three_of_a_kind() {
		// well apparently if you had a full house, you wouldn't be here
		// so you can continue to use the odd man out removal pattern....
		// and I used a ridiculously complicated removal pattern for one pair 
		// and it turns out this thing kinda works for everything
		// gonna git commit these comments because wtf and I think it's funny.
		return handle_two_pair();
	}
	private int handle_four_of_a_kind() {
		// both four of a kind and two pair 
		// implement an odd man out deletion pattern
		return handle_two_pair();
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
					/*
					System.out.printf("%d has 4 FLUSH!!!!!!!!!!!!!!11\n", this.player_id());
					this.print_hand();
					System.out.printf("Words");
					//*/
					// discard non similar
				}
				
				// 4 cards in sequence?
				else if(this.is_straight(4)) {
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
					// remap score to rank_map
					//		score	rank_map[rank]		rank	
					//		1		A				->	0
					//		2		2				->	1
					//		3		3				->	2
					//		...		...					...
					//		13		K				->	13
					// func(score) = score - 1

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
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return bot_logic();
	}

}
