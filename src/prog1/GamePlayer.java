package prog1;

import java.util.Scanner;

// TODO rename functions with prefix indicating what it's modifying

public class GamePlayer {
	protected Hand my_hand; //make private later
	private int player_id;
	private Deck discard = new Deck();
	private boolean first_prompt = true;
	Scanner input = new Scanner(System.in).useDelimiter(" ");



	protected char[] suit_map = {	'C', 'D', 'S', 'H'};

	protected char[] rank_map = {	'A', '2', '3', '4', 
									'5', '6', '7', '8', 
									'9', 'T', 'J', 'Q', 
									'K'};
	/*
	private int char_to_suit(char suit) {
		switch(suit) {
		case 'C':
			return 0;
		case 'D':
			return 1;
		case 'S':
			return 2;
		case 'H':
			return 3;
		}
		return -1;
	}
	private int char_to_rank(char rank) {
		switch(rank) {
		case 'A':
			return 0;
		case '2':
			return 1;
		case '3':
			return 2;
		case '4':
			return 3;
		case '5':
			return 4;
		case '6':
			return 5;
		case '7':
			return 6;
		case '8':
			return 7;
		case '9':
			return 8;
		case 'T':
			return 9;
		case 'J':
			return 10;
		case 'Q':
			return 11;
		case 'K':
			return 12;
		}
		return -1;
	}
	*/

	public GamePlayer(int id, int num_cards) {
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
	private int ui() {
		int id_discard = -1;
		int ace_exception = 0;
		System.out.printf("DISCARD LEN: %d\n", discard.get_num_cards());
		if(first_prompt) {
			System.out.printf("STOP SKIPPING ME!!!!\n");
			System.out.printf("It's your turn\n");
			if(my_hand.find_first_rank(0) != -1) {
				ace_exception = 1;
				System.out.printf("You have an Ace so you may swap an additional card\n");
			}
			System.out.printf("Select %d cards(1-%d) to swap\n", my_hand.get_num_cards() - 2 + ace_exception, my_hand.get_num_cards());
			ui_display_hand();
			System.out.printf("Cards to swap: ");
			// TODO INPUT
			// create a deck of cards to remove
			handle_input(my_hand.get_num_cards() - 2 + ace_exception);
			first_prompt = false;
		}
		if(discard.get_num_cards() == 0) {
			first_prompt = true;
			return -1;
		}
		else if(discard.get_num_cards() > 0) {
			Card drawn = discard.draw_card();
			id_discard = my_hand.find(drawn.get_rank(), drawn.get_suit());
		}

		/*
		 * TODO interface
		 */
		// set id_discard to the card you want to discard
		return id_discard;
	}
	private void handle_input(int discards_left) {
		int id_discard = -1;
		if(input.hasNextLine() && discard.get_num_cards() < discards_left) {
			System.out.printf("discards left %d num cards %d\n", discards_left, discard.get_num_cards());
			String[] input_line = input.nextLine().split(" ");
			System.out.printf("Args: \n");
			for(int i = 0; i < input_line.length; i++) {
				id_discard = Integer.parseInt(input_line[i]) - 1;

				if(id_discard >= 0 && id_discard < my_hand.get_num_cards())
					discard.place_card(my_hand.get_card(id_discard));
			}
		}
		
		return;
	}
	public Card discard(int index){
		return my_hand.discard(index);
	}
	public int turn() {
		return ui();
	}

	private void ui_display_hand() {
		// draw a card, print the card, then put it at the bottom
		for(int i = 0; i < my_hand.get_num_cards(); i++) {
			if(my_hand.get_suit(i) % 2 == 0)
				//black
				;
			if(my_hand.get_suit(i) % 2 == 1)
				//red
				;
			System.out.printf("%d) %c%c\t", i+1, rank_map[my_hand.get_rank(i)], suit_map[my_hand.get_suit(i)]);
		}
		System.out.printf("\n");
		return;
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
		int score = get_high_card();
		if(is_straight(5) && is_flush(5)) {
			score = 21;
		}
		else if(is_straight(5)) {
			score = 17;
		}
		else if(is_flush(5)) {
			score = 18;
		}

		// handle rankings based on duplicates
		int ret_val = is_pair();
		switch(ret_val) {
		case 0:
			break;
		case 1:
			score = Math.max(14, score);
			break;
		case 2:
			score = Math.max(15, score);
			break;
		case 3:
			score = Math.max(16, score);
			break;
		case 4:
			score = Math.max(20, score);
			break;
		case 5:
			score = Math.max(19, score);
			break;
		}
		return score;
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
	protected int count_by_rank(int rank) {
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

}
