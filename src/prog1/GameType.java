package prog1;
import java.util.Random;

public class GameType {
	static private Random rng = new Random();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck game_deck = make_deck();
		Deck discard_deck = new Deck();
		System.out.printf("deck_len: %d\n", game_deck.get_size());
		print_deck(game_deck);
		System.out.printf("deck_len: %d\n", game_deck.get_size());
		print_deck(game_deck);
		System.out.printf("deck_len: %d\n", game_deck.get_size());
		shuffle_deck(game_deck);
		System.out.printf("deck_len: %d\n", game_deck.get_size());
		print_deck(game_deck);
		System.out.printf("deck_len: %d\n", game_deck.get_size());
		return;
	}
	private static Deck make_deck() {
		char[] suit_map = {	'C', 'D', 'S', 'H'};

		char[] rank_map = {	'A', '1', '2', '3', 
							'4', '5', '6', '7', 
							'8', '9', 'T', 'J', 
							'Q', 'K'
							};
		System.out.println("make_deck()");
		Deck game_deck = new Deck();
		for(int suit = 0; suit < 4; suit++) {
			for(int rank = 1; rank < 14; rank++) {
				Card my_card = new Card(rank_map[rank], suit_map[suit]);
				game_deck.place_card(my_card);
				//System.out.printf("card: suit: %d rank: %d\n", my_card.get_suit(), my_card.get_rank());
			}
		}
		return game_deck;
	}
	

	private static void shuffle_deck(Deck game_deck) {
		System.out.println("shuffle_deck()");
		Deck shuffled = new Deck();
		for(int i = game_deck.get_size(); i > 0; --i) {
			int random_index = rng.nextInt(i); // TODO check edge case
			Card drawn = extract_ith_card(game_deck, random_index);
			shuffled.place_card(drawn); // for 401 figure out what the shuffling problem's runtime is
		}
		game_deck.combine(shuffled);
		return;
	}
	private static void print_deck(Deck game_deck) {
		System.out.println("print_deck()");
		// draw a card, print the card, then put it at the bottom
		for(int i = 0; i < game_deck.get_size(); i++) {
			Card drawn = game_deck.draw_card();
				System.out.printf("%c of %c\n", drawn.get_rank(), drawn.get_suit());
			game_deck.place_card_bottom(drawn);
		}
		return;
	}
	// starts from 0
	private static Card extract_ith_card(Deck game_deck, int index) {
		Deck temp = new Deck();
		Card drawn;

		// split the deck until we get the card we want
		for(int i = 0; i < index; i++) {
			temp.place_card_bottom(game_deck.draw_card());
		}

		// draw a card
		drawn = game_deck.draw_card();

		// put the decks back together
		temp.combine(game_deck);
		game_deck.combine(temp);
		
		return drawn;
	}

}
