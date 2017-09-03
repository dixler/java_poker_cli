package prog1;
import java.util.Random;

public class GameType {
	static private Random rng = new Random();
	static int num_bots = 3; // have dynamically determined at runtime
	static int num_players = num_bots + 1; // have dynamically determined at runtime
	static int num_cards = 5;
	static int max_discard = 3;
	static Deck game_deck;
	static Deck discard_deck;
	static char[] suit_map = {	'C', 'D', 'S', 'H'};

	static char[] rank_map = {	'A', '2', '3', '4', 
								'5', '6', '7', '8', 
								'9', 'T', 'J', 'Q', 
								'K'};

	public static void main(String[] args) {
		// TODO take command line args or something
		game_deck = make_deck();
		discard_deck = new Deck();

		// check if there are too many players
		if(game_deck.get_num_cards() < num_players * num_cards) {
			System.out.printf("You have too many friends");
			return;
		}
		
		// create the deck

		GamePlayer[] players = new GamePlayer[num_players];
		// players[0] = new GamePlayer();
		// TODO remember to add human player
		
		
		for(int i = 0; i < num_players; i++) {
			players[i] = new GameBot(i, num_cards);
		}

		// TODO handle too many players
		//print_deck(game_deck);
		shuffle_deck(game_deck);
		//print_deck(game_deck);

		// Begin game
		
		for(int round = 0; round < 2; round++) {
			System.out.printf("Round %d\n", round);
			//print_deck(game_deck);

			round_init(players);
			// Discard cards
			//print_hands(players);
			/*
			for(int i = 0; i < rank_map.length-4; i++) {
				AI_near_straight(players[0], i);
			}
			//*/
			for(int part = 0; part < 1; part++) {

				// For each player allow them to discard
				for(int i = 0; i < num_players; i++) {
					int num_discarded = 0;
					int id_discarded;
					do {
						// PRE DISCARD TURN //////////////////////////////////////
						/*
						System.out.printf("PRE DISCARD\n");
						players[i].print_hand();
						//*/
						//////////////////////////////////////////////////////////
						//System.out.printf("length: %d\n", players[i].hand_size());

						id_discarded = players[i].turn();
						//System.out.printf("DISCARDING %d\n", id_discarded);


						if(id_discarded != -1) {
							discard_deck.place_card(players[i].discard(id_discarded));
							num_discarded += 1;
							/*
							System.out.printf("DISCARDING\n");
							System.out.printf("DISCARDIND\n");
							System.out.printf("DISCARDIND\n");
							System.out.printf("DISCARDIND\n");
							System.out.printf("DISCARDIND\n");
							System.out.printf("DISCARDIND\n");
							System.out.printf("DISCARDIND\n");
							//*/
						}

						// POST DISCARD TURN //////////////////////////////////////
						/*
						System.out.printf("POST DISCARD\n");
						players[i].print_hand();
						//*/
						///////////////////////////////////////////////////////////

					}while(id_discarded != -1 && num_discarded < (max_discard + ace_exception(players[i])));
					System.out.printf("Computer Player %d discarded %d cards\n", players[i].get_player_id(), num_discarded);
					
					
					
					// deal cards to player until hand is full again
					while(players[i].get_hand_size() < num_cards) {
						// if we run out of cards somehow, we 
						// shuffle // the discard deck and use 
						// it

						if(game_deck.get_num_cards() == 0) {
							// Shuffle the discard pile
							discard_deck.shuffle_deck(rng);
							// Place the shuffled discard pile under the deck
							game_deck.combine(discard_deck);
						}
						// deal a card from the deck
						players[i].draw_card(game_deck.draw_card());
						
					}
				}
				//*/
			}
			/*
			for(int i = 0; i < rank_map.length; i++) {
				HACK_straight(players[0], i);
				print_hands(players);
			}
			//*
			for(int i = 0; i < suit_map.length; i++) {
				HACK_flush(players[0], i);
				print_hands(players);
			}
			//*/

			print_hands(players);

			for(int i = 0; i < suit_map.length; i++) {
				print_hands(players);
			}
			//*/
			print_hands(players);

			// Discard the cards for all of the players
			for(int i = 1; i <= num_cards; i++) {
				for(int j = 0; j < num_players; j++) {
					discard_deck.place_card(players[j].discard(num_cards - i));
				}
			}

			// if we don't have enough cards to play another round
		}

		return;
	}
	private static Deck make_deck() {
		System.out.println("make_deck()");
		Deck game_deck = new Deck();

		// create a card and then put it into the deck
		for(int suit = 0; suit < suit_map.length; suit++) {
			for(int rank = 0; rank < rank_map.length; rank++) {
				Card my_card = new Card(rank, suit);
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
		System.out.printf("deck_len: %d\n", game_deck.get_num_cards()); // TODO remove this
		game_deck.print();
		return;
	}
	// DEBUG
	private static void print_hands(GamePlayer[] players) {
		for(int i = 0; i < num_players; i++) {
			int score = players[i].eval_score();
			switch(players[i].eval_score()) {
				case 21:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Straight Flush!\n", i);
					break;
				case 20:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Four of a Kind!\n", i);
					break;
				case 19:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Full House\n", i);
					break;
				case 18:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Flush!\n", i);
					break;
				case 17:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Straight!\n", i);
					break;
				case 16:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Three of a Kind!\n", i);
					break;
				case 15:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("Two Pair!\n", i);
					break;
				case 14:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("One Pair!\n", i);
					break;
				default:
					System.out.printf("Player %d's hand':\n", i);
					players[i].print_hand();
					System.out.printf("High Card: %c!\n", rank_map[score-1]);
					break;
			}
		}
		return;
	}
	private static void round_init(GamePlayer[] players) {
		// if we don't have enough cards after the last round
		// add the discard pile to the deck
		if(game_deck.get_num_cards() < num_players * num_cards) {
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
				//System.out.printf("round init: dealing: %c of %c\n", drawn.get_rank(), drawn.get_suit());
				players[j].draw_card(drawn);
			}
		}
	}
	private static int ace_exception(GamePlayer player) {
		for(int i = 0; i < suit_map.length; i++) {
			if(player.is_holding('A', suit_map[i]))
				return 1;
		}
		return 0;

	}
	private static void AI_near_straight(GamePlayer player, int index) {
		System.out.printf("HACK_test_straight\n");
		for(int i = 0; i < num_cards-1; i++) {
			player.my_hand.peek(i).set_rank((index+i)%13);
		}
		return;
	}
	private static void HACK_straight(GamePlayer player, int index) {
		System.out.printf("HACK_test_straight\n");
		for(int i = 0; i < num_cards; i++) {
			player.my_hand.peek(i).set_rank((index+i)%13);
		}
		return;
	}
	private static void HACK_flush(GamePlayer player, int index) {
		System.out.printf("HACK_test_flush\n");
		for(int i = 0; i < num_cards; i++) {
			player.my_hand.peek(i).set_suit(index);
		}
		player.print_hand();
		return;
	}

}
