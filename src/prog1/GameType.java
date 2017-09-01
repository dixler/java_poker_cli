package prog1;
import java.util.Random;

public class GameType {
	static private Random rng = new Random();
	static int num_bots = 1; // have dynamically determined at runtime
	static int num_cards = 5;
	static int max_swaps = 3;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Deck game_deck = make_deck();
		Deck discard_deck = new Deck();
		GamePlayer human = new GamePlayer();


		GameBot[] bots = new GameBot[num_bots];
		for(int i = 0; i < num_bots; i++) {
			bots[i] = new GameBot();
		}
		print_deck(game_deck);
		print_deck(game_deck);
		shuffle_deck(game_deck);
		print_deck(game_deck);
		

		// Begin game
		
		for(int round = 0; round < 99; round++) {

			// Draw the cards for all of the players
			for(int i = 0; i < num_cards; i++) {
				Card	drawn = game_deck.draw_card();
				human.take_card(drawn);
				for(int j = 0; j < num_bots; j++) {
							drawn = game_deck.draw_card();
							bots[j].take_card(drawn);
				}
			}
			// Swap cards
			for(int part = 0; part < 2; part++) {
				/*
				 * AI calculate discard(move to GameBot in future
				 */
				//*
				System.out.printf("PRE SWAP\n");
				print_hands(bots, human);
				for(int i = 0; i < num_bots; i++) {
					int cur_swaps = 0;
					while(bots[i].is_working() == 1) {
						// handle ace exception
						/*
						 * 
						 * TODO
						 */
						int returned = bots[i].swap();
						System.out.printf("length: %d\n", bots[i].hand_size());
						// swap automatically extracts a card so 
						// we need to replace it
						// if the bot is still picking cards to remove, then returned
						// is a card we want to discard
						if(bots[i].is_working() == 1) {
							discard_deck.place_card(bots[i].return_card(returned));
							cur_swaps += 1;
						}
					}
					// draw cards until hand is full again
					while(bots[i].hand_size() < num_cards) {
						// if we run out of cards somehow, we 
						// shuffle // the discard deck and use 
						// it

						if(game_deck.get_size() == 0) {
							// Shuffle the discard pile
							discard_deck.shuffle_deck(rng);
							// Place the shuffled discard pile under the deck
							game_deck.combine(discard_deck);
						}
						// deal a card from the deck
						bots[i].take_card(game_deck.draw_card());
						
					}
				}
				System.out.printf("POST SWAP\n");
				print_hands(bots, human);
				//*/
			}

			/*
			 * debug hand drawing
			 */
			//print_hands(bots, human);
			/*
			*/
			
			// Discard the cards for all of the players
			for(int i = 0; i < num_cards; i++) {
				discard_deck.place_card(human.return_card(0));
				for(int j = 0; j < num_bots; j++) {
					discard_deck.place_card(bots[j].return_card(0));
				}
			}
			// Shuffle the discard pile
			discard_deck.shuffle_deck(rng);
			// Place the shuffled discard pile under the deck
			game_deck.combine(discard_deck);
		}

		

		

		
		
		//
		
		
		
		

		return;
	}
	private static Deck make_deck() {
		System.out.println("make_deck()");

		char[] suit_map = {	'C', 'D', 'S', 'H'};

		char[] rank_map = {	'A', '2', '3', '4', 
							'5', '6', '7', '8', 
							'9', 'T', 'J', 'Q', 
							'K'};
		Deck game_deck = new Deck();

		// create a card and then put it into the deck
		for(int suit = 0; suit < 4; suit++) {
			for(int rank = 0; rank < 13; rank++) {
				Card my_card = new Card(rank_map[rank], suit_map[suit]);
				game_deck.place_card(my_card);
			}
		}

		return game_deck;
	}
	
	private static void shuffle_deck(Deck game_deck) {
		System.out.println("shuffle_deck()");
		
		game_deck.shuffle_deck(rng);

		return;
	}

	private static void print_deck(Deck game_deck) {
		System.out.println("print_deck()");
		System.out.printf("deck_len: %d\n", game_deck.get_size()); // TODO remove this
		game_deck.print();
		return;
	}
	// starts from 0
	private static void print_hands(GameBot[] bots, GamePlayer human) {
			System.out.printf("HAND: human\n");
			human.print_hand();
			for(int i = 0; i < num_bots; i++) {
				System.out.printf("HAND: bot %d\n", i);
				bots[i].print_hand();
			}
	}

}
