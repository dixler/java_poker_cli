package prog1;

import java.util.Scanner;

// TODO rename functions with prefix indicating what it's modifying

public class GamePlayer extends GameBot{
	Scanner input = new Scanner(System.in).useDelimiter(" ");

	public GamePlayer(int id, int num_cards) {
		super(id, num_cards);
		return;
	}
	private Deck ui(int max_swaps) {
		System.out.printf("It's your turn\n");
		if(my_hand.find_first_rank('A') != -1) {
			System.out.printf("You have an Ace so you may swap an additional card\n");
		}
		System.out.printf("Select %d cards(1-%d) to swap followed by -1\n", max_swaps, my_hand.get_num_cards());
		ui_display_hand();
		System.out.printf("Cards to swap: ");
		// TODO INPUT
		// create a deck of cards to remove
		handle_input(max_swaps);
		for(int i = 0; i < discard.get_num_cards(); i++) {
			Card discarded = discard.draw_card();
			my_hand.discard(my_hand.find(discarded.get_rank(), discarded.get_suit()));
			discard.place_card_bottom(discarded);
		}

		// set id_discard to the card you want to discard
		return discard;
	}
	private void handle_input(int max_swaps) {
		int id_discard = -1;
		if(input.hasNextLine() && discard.get_num_cards() < max_swaps) {
			String[] input_line = input.nextLine().split(" ");
			System.out.printf("Args: \n");
			for(int i = 0; i < input_line.length && i < max_swaps; i++) {
				id_discard = Integer.parseInt(input_line[i]) - 1;

				if(id_discard >= 0 && id_discard < my_hand.get_num_cards()) {
					discard.place_card(my_hand.get_card(id_discard));
					my_hand.discard(id_discard);
				}
				else if(id_discard == -1)
					return;
			}
		}
		
		return;
	}
	public Deck turn(int max_swaps) {
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
}
