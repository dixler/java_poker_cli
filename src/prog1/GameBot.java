package prog1;

public class GameBot extends GamePlayer{
	public GameBot(int id, int num_cards) {
		super(id, num_cards);
	}
	
	
	private int handle_one_pair() {
		return -1;
	}
	private int handle_two_pair() {
		return -1;
	}
	private int handle_three_of_a_kind() {
		return -1;
	}
	private int handle_four_of_a_kind() {
		return -1;
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
					System.out.printf("High Card!\n");
					boolean has_ace = (score == 1);
					int high_card_index = 0;
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
					if(has_ace)
						// we want to keep the ace
						kept_card_rank = 0;

					for(int i = 0; i < this.my_hand.get_num_cards(); i++) {
						// find the high card
						if(this.my_hand.peek(i).get_rank() == kept_card_rank) {
							high_card_index = i;
							break;
						}
					}
					System.out.printf("Ace Exception\n");
					this.print_hand();
					if(high_card_index > 0) {
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
