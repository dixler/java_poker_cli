package prog1;
import java.util.Random;

public class GameType {
	static Random rng = new Random();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck game_deck = make_deck();
		Deck discard_deck = new Deck();
		print_deck(game_deck);
		print_deck(game_deck);
		shuffle_deck(game_deck);
		print_deck(game_deck);
		return;
	}
	private static Deck make_deck() {
		System.out.println("make_deck()");
		Deck game_deck = new Deck();
		for(int suit = 0; suit < 4; suit++) {
			for(int rank = 1; rank < 14; rank++) {
				Card my_card = new Card(rank, suit);
				game_deck.place_card(my_card);
				//System.out.printf("card: suit: %d rank: %d\n", my_card.get_suit(), my_card.get_rank());
			}
		}
		return game_deck;
	}
	

	private static void shuffle_deck(Deck game_deck) {
		System.out.println("shuffle_deck()");
		Deck shuffled = new Deck();
		Card drawn = extract_ith_card(game_deck, 26);
		game_deck.place_card(drawn);
		return;
		/*
		for(int i = 52; i > 0; --i) {
			int random_index = rng.nextInt(i); // TODO check edge case
			Card drawn = extract_ith_card(game_deck, random_index);
			shuffled.place_card(drawn); // for 401 figure out what the shuffling problem's runtime is
		}
		game_deck.combine(shuffled);
		return;
		*/
	}
	private static void print_deck(Deck game_deck) {
		System.out.println("print_deck()");
		for(int i = 0; i < 52; i++) {
			Card drawn = game_deck.draw_card();
			System.out.printf("card: suit: %d rank: %d\n", drawn.get_suit(), drawn.get_rank());
			game_deck.place_card_bottom(drawn);
		}
		return;
	}
	// starts from 0
	private static Card extract_ith_card(Deck game_deck, int index) {
		Deck temp = new Deck();
		Card drawn;
		for(int i = 0; i < index; i++) {
			System.out.printf("\nindex %d", i);
			temp.place_card_bottom(game_deck.draw_card());
		}
		drawn = game_deck.draw_card();
		temp.combine(game_deck);
		game_deck.combine(temp);

		
		return drawn;
	}

}
