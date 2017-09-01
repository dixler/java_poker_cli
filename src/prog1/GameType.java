package prog1;
import java.util.Random;

public class GameType {
	static private Random rng = new Random();
	static int num_bots = 1; // have dynamically determined at runtime
	static int num_players = num_bots + 1; // have dynamically determined at runtime
	static int num_cards = 5;
	static int max_swaps = 3;
	static Deck game_deck;
	static Deck discard_deck;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		game_deck = make_deck();
		discard_deck = new Deck();

		GamePlayer[] players = new GamePlayer[num_players];
		//players[0] = new GamePlayer();
		// TODO add human player
		for(int i = 0; i < num_players; i++) {
			players[i] = new GameBot(i);
		}
		// TODO handle too many players
		print_deck(game_deck);
		shuffle_deck(game_deck);
		print_deck(game_deck);

		// Begin game
		
		for(int round = 0; round < 52; round++) {
			print_deck(game_deck);

			round_init(players);
			// Swap cards
			for(int part = 0; part < 2; part++) {
				/*
				 * AI calculate discard(move to GameBot in future
				 */
				//*
				System.out.printf("PRE SWAP\n");
				print_hands(players);
				for(int i = 0; i < num_players; i++) {
					players[i].start_working();
					int num_discarded = 0;

					while(players[i].is_working()) {
						// handle ace exception
						if(		players[i].hand_size() > (num_cards - max_swaps)
							&&	has_ace(players[i])) {

							System.out.printf("Computer Hand contains Ace\n");
						}
							
						/*
						 * 
						 * TODO
						 */
						int returned = players[i].discard();
						System.out.printf("length: %d\n", players[i].hand_size());
						// swap automatically extracts a card so 
						// we need to replace it
						// if the bot is still picking cards to remove, then returned
						// is a card we want to discard
						if(players[i].is_working()) {
							discard_deck.place_card(players[i].return_card(returned));
							num_discarded += 1;
						}
					}
					// draw cards until hand is full again
					while(players[i].hand_size() < num_cards) {
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
						players[i].take_card(game_deck.draw_card());
						
					}
				}
				System.out.printf("POST SWAP\n");
				print_hands(players);
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
				for(int j = 0; j < num_players; j++) {
					discard_deck.place_card(players[j].return_card(0));
				}
			}

			// if we don't have enough cards to play another round
		}

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
	// DEBUG
	private static void print_hands(GamePlayer[] players) {
		for(int i = 0; i < num_players; i++) {
			System.out.printf("Player %d's hand':\n", i);
			players[i].print_hand();
		}
		return;
	}
	private static void round_init(GamePlayer[] players) {
		// if we don't have enough cards after the last round
		// add the discard pile to the deck
		if(game_deck.get_size() < num_players * num_cards) {
			// Place the discard deck under the deck
			game_deck.combine(discard_deck);
			// Shuffle the deck 
			game_deck.shuffle_deck(rng);
		}

		// deal the cards
		for(int i = 0; i < num_cards; i++) {
			Card	drawn;
			for(int j = 0; j < num_players; j++) {
				drawn = game_deck.draw_card();
				players[j].take_card(drawn);
			}
		}
	}
	private static boolean has_ace(GamePlayer player) {
		return	((
					player.hand_contains('A', 'C') ||
					player.hand_contains('A', 'D') ||
					player.hand_contains('A', 'S') ||
					player.hand_contains('A', 'H'))
				);
	}

}
