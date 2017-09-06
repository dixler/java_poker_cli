package prog1;
import java.util.Random;

public class GameType {
	static private Random rng = new Random();
	static int num_bots = 4; // have dynamically determined at runtime
	static int num_humans = 0; // have dynamically determined at runtime
	static int num_players = num_humans + num_bots; // have dynamically determined at runtime
	static int num_cards = 5;
	static int max_discard = 3;
	static Deck game_deck;
	static Deck discard_deck;
	static char[] suit_map = {	'C', 'D', 'S', 'H'};

	static char[] rank_map = {	'2', '3', '4', '5', 
								'6', '7', '8', '9', 
								'T', 'J', 'Q', 'K', 
								'A'};

	public static void main(String[] args) {
		// TODO take command line args or something
		game_deck = make_deck();
		discard_deck = new Deck();

		// check if there are too many players
		if(game_deck.get_num_cards() < num_players * num_cards) {
			System.out.printf("You have too many friends\n");
			return;
		}
		
		// create the deck

		GameBot[] players = new GameBot[num_players];
		//int num_humans = 0;
		players[0] = new GamePlayer(0, num_cards);
		// TODO remember to add human player
		
		for(int i = 1; i < num_players; i++) {
			players[i] = new GameBot(i, num_cards);
		}

		// TODO handle too many players
		//print_deck(game_deck);
		shuffle_deck(game_deck);
		//print_deck(game_deck);

		// Begin game
		for(int round = 0; round < 400; round++) {
			System.out.printf("Round %d\n", round);
			//game_deck.print();
			//discard_deck.print();

			round_init(players);
			print_hands(players);

			// For each player allow them to discard
			for(int i = 0; i < players.length; i++) {
				Deck discard_pile;

				discard_pile = players[i].turn(max_discard + ace_exception(players[i]));
				int num_discarded = discard_pile.get_num_cards();

				discard_deck.combine(discard_pile);

				System.out.printf("Computer Player %d discarded %d cards\n", players[i].get_player_id(), num_discarded);
				
				
				
				// deal cards to player until hand is full again
				while(players[i].get_hand_size() < num_cards) {
					// if we run out of cards somehow, we 
					// shuffle the discard deck and use it

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
			/*
			for(int i = 0; i < rank_map.length; i++) {
				HACK_straight(players[0], i);
				print_hands(players);
			}
			/*
			for(int i = 0; i < suit_map.length; i++) {
				HACK_flush(players[0], i);
				print_hands(players);
			}
			//*/

			/*
			print_hands(players);

			for(int i = 0; i < suit_map.length; i++) {
				print_hands(players);
			}
			//*/
			int winner = -1;
			int max_score = -1;
			int max_high_card = -1;
			boolean tie = false;
			for(int i = 0; i < players.length; i++) {
				int cur_score = players[i].eval_score();
				if(max_score < cur_score) {
					tie = false;
					max_score = cur_score;
					max_high_card = players[i].get_high_card();
					winner = i;
				}
				else if(max_score == cur_score) {
					int cur_high_card = players[i].get_high_card();
					// is the high_card the same?
					if(max_high_card == cur_high_card)
						tie = true;
					// ah new high card
					else if(max_high_card < cur_high_card) {
						max_high_card = players[i].get_high_card();
						winner = i;
						tie = false;
					}
				}
			}
			if(tie) {
				System.out.printf("Tie!\n");
			}
			else {
				System.out.printf("Player %d wins!\n", winner);
			}
			print_hands(players);

			// Discard the cards for all of the players
			for(int i = 0; i < num_players; i++) {
				while(players[i].get_hand_size() > 0) {
					discard_deck.place_card(players[i].discard(0));
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

	private static void print_hands(GameBot[] players) {
		for(int i = 0; i < num_players; i++) {
			System.out.printf("Player %d's hand':\n", i);
			// evaluation of score is the win type * the high card
			// score = win*high_card
			// score
			int score = players[i].eval_score();
			int high_card_val = players[i].get_high_card(); //handles ace high exception
			int high_card_index = high_card_val % 13; //handles ace high exception
			//System.out.printf("score: %d\n", score);
			switch(score) {
				case 21:
					players[i].print_hand();
					System.out.printf("Straight Flush!\n", i);
					break;
				case 20:
					players[i].print_hand();
					System.out.printf("Four of a Kind!\n", i);
					break;
				case 19:
					players[i].print_hand();
					System.out.printf("Full House\n", i);
					break;
				case 18:
					players[i].print_hand();
					System.out.printf("Flush!\n", i);
					break;
				case 17:
					players[i].print_hand();
					System.out.printf("Straight!\n", i);
					break;
				case 16:
					players[i].print_hand();
					System.out.printf("Three of a Kind!\n", i);
					break;
				case 15:
					players[i].print_hand();
					System.out.printf("Two Pair!\n", i);
					break;
				case 14:
					players[i].print_hand();
					System.out.printf("One Pair!\n", i);
					break;
				default:
					players[i].print_hand();
					break;
			}
			System.out.printf("High Card: %c!\n", rank_map[high_card_index]);
		}
		return;
	}
	private static void round_init(GameBot[] players) {
		// if we don't have enough cards after the last round
		// add the discard pile to the deck
		if(game_deck.get_num_cards() < num_players * num_cards) {
			// Place the discard deck under the deck
			game_deck.combine(discard_deck);
			// Shuffle the deck 
			//game_deck.shuffle_deck(rng);
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
	private static int ace_exception(GameBot players) {
		if(players.count_by_rank('A') > 0) {
			return 1;
		}
		return 0;

	}
	/*
	private static void print_deck(Deck game_deck) {
		System.out.println("print_deck()");
		System.out.printf("deck_len: %d\n", game_deck.get_num_cards()); // TODO remove this
		for(int i = 0; i < game_deck.get_num_cards(); i++) {
			Card drawn = game_deck.draw_card();
			System.out.printf("%c of %c\n", rank_map[drawn.get_rank()], suit_map[drawn.get_suit()]); // TODO remove this
			game_deck.place_card_bottom(drawn);
		}
		return;
	}
	private static void AI_near_straight(GamePlayer player, int index) {
		System.out.printf("HACK_test_straight\n");
		for(int i = 0; i < num_cards-1; i++) {
			player.my_hand.get_card(i).set_rank((index+i)%13);
		}
		return;
	}
	private static void HACK_straight(GamePlayer player, int index) {
		System.out.printf("HACK_test_straight\n");
		for(int i = 0; i < num_cards; i++) {
			player.my_hand.get_card(i).set_rank((index+i)%13);
		}
		return;
	}
	private static void HACK_flush(GamePlayer player, int index) {
		System.out.printf("HACK_test_flush\n");
		for(int i = 0; i < num_cards; i++) {
			player.my_hand.get_card(i).set_suit(index);
		}
		player.print_hand();
		return;
	}
	//*/

}
