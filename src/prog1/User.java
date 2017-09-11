package prog1;

import java.util.Scanner;

// TODO rename functions with prefix indicating what it's modifying

public class User extends Player{
	Scanner input = new Scanner(System.in).useDelimiter(" ");

	public User(int id, int num_cards) {
		super(id, num_cards);
		return;
	}
	// User interface creates a pile of cards to remove from the hand in a 
	// manner to emulate the bot discard methods
	private Pile ui(int max_swaps) {
		System.out.printf("It's your turn\n\n");
		if(my_hand.find_first_rank(12) != -1) { // TODO Ace currently hardcoded
			System.out.printf("You have an Ace so you may hold onto your ace\n"
							+ "and discard the other cards additional card\n"
							+ "(discarding the Ace with 4 cards will only\n"
							+ "discard the first 3 cards)\n\n");
		}
		System.out.printf("Select %d cards(1-%d) to swap then press \n"
						+ "enter(-1 to keep your hand).\n\n", max_swaps, my_hand.get_num_cards());
		ui_display_hand();
		System.out.printf("Cards to swap: ");
		// TODO INPUT
		// create a deck of cards to remove
		discard.removeAll();
		handle_input(max_swaps);

		for(int i = 0; i < max_swaps && i < discard.get_num_cards(); i++) {
			Card discarded = discard.draw_card();
			int found_index = my_hand.find(discarded.get_rank(), discarded.get_suit());
			if(i < 3) {
				my_hand.discard(my_hand.find(discarded.get_rank(), discarded.get_suit()));
				discard.place_card_bottom(discarded);
			}
			// ON ACE EXCEPTION
			else if(i == 3) {
				// check that (there is an ace and the discarded card is not an ace) || there is more than 1 ace
				if(		this.count_by_rank('A') > 1
					|| 	(this.count_by_rank('A') == 1 && rank_map[my_hand.get_card(found_index).get_rank()] != 'A')
				) {
					my_hand.discard(my_hand.find(discarded.get_rank(), discarded.get_suit()));
					discard.place_card_bottom(discarded);
				}
				else {
					return discard;
				}
			}


		}

		// set id_discard to the card you want to discard
		return discard;
	}
	// push selected cards to a deck
	private void handle_input(int max_swaps) {
		int id_discard = -1;
		if(input.hasNextLine() && discard.get_num_cards() < max_swaps) {
			String[] input_line = input.nextLine().split(" ");
			//System.out.printf("Args: \n");
			for(int i = 0; i < input_line.length && i < max_swaps; i++) {
				id_discard = Integer.parseInt(input_line[i]) - 1;
				//System.out.printf("Args: %d\n", id_discard);

				if(id_discard >= 0 && id_discard < my_hand.get_max_cards()) {
					//System.out.printf("max_cards: %d\n", my_hand.get_max_cards());
					//System.out.printf("handle_input: %c %c\n", rank_map[my_hand.get_rank(id_discard)], suit_map[my_hand.get_suit(id_discard)]);
					discard.place_card_bottom(my_hand.get_card(id_discard));
					//my_hand.discard(id_discard);
				}
			}
		}
		
		return;
	}
	public Pile turn(int max_swaps) {
		return ui(max_swaps);
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

}
