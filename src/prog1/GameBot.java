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
		if(score >= 0) {
			switch(score) {
			case 7:
				// No work to be done STRAIGHT FLUSH
				break;
			case 6:
				id_discard = handle_four_of_a_kind();
				break;
			case 5:
				// No work to be done FULL HOUSE
				break;
			case 4:
				// No work to be done FLUSH
				break;
			case 3:
				// No work to be done STRAIGHT
				break;
			case 2:
				id_discard = handle_three_of_a_kind();
				break;
			case 1:
				id_discard = handle_two_pair();
				break;
			case 0:
				id_discard = handle_one_pair();
				break;
			}
		}
		else {
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
				boolean has_ace = (score == -1);
				int high_card_index = 0;
				for(int i = 0; i < this.my_hand.get_num_cards(); i++) {
					// TODO remap scores to sane numbers
					// remap score to rank_map
					//		score	rank_map[rank]		rank	
					//		-1		K				->	12
					//		-2		Q				->	11
					//		-3		J				->	10
					//		...		...					...
					//		-13		A				->	0
					// find func(score) = i;
					// func(score) = -1*score - 1

					if(this.my_hand.peek(i).get_rank() == -1*score - 1) {
						high_card_index = i;
						break;
					}
				}
				if(has_ace) {
					System.out.printf("Ace Exception\n");
					this.print_hand();
					if(high_card_index > 0) {
						// we want to discard 0 as to move the ace index down
						id_discard = 0;
					}
					else {
						// ace is at the 0 index so we can remove index 1 
						// repeatedly since discarding shifts indices 
						// down
						id_discard = 1;
					}
				}
				else {
				}
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
