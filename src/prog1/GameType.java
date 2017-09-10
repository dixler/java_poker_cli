package prog1;
import java.util.Random;
import java.util.Scanner;

public class GameType {
	static private Random rng = new Random();
	static int num_cards = 5;
	static int max_discard = 3;

	static Deck game_deck;
	static Deck discard_deck;
	static GameBot[] players;
	

	static char[] suit_map = {	'C', 'D', 'S', 'H'};

	static char[] rank_map = {	'2', '3', '4', '5', 
								'6', '7', '8', '9', 
								'T', 'J', 'Q', 'K', 
								'A'};

	public static void main(String[] args) {
		/*
		Scanner setup_input = new Scanner(System.in);
		System.out.printf("Number of computer players: ");
		int num_bots = setup_input .nextInt();
		int num_players = 1 + num_bots; // have dynamically determined at runtime
		*/
		// TEMPORARY
		int num_players = 4; // have dynamically determined at runtime


		// TODO take command line args or something
		game_deck = make_deck();
		discard_deck = new Deck();

		// check if there are too many players
		if(game_deck.get_num_cards() < num_players * num_cards) {
			System.out.printf("You have too many friends\n");
			//setup_input.close();
			return;
		}
		
		// create the deck

		players = new GameBot[num_players];

		//players[0] = new GamePlayer(0, num_cards);
		// TODO remember to add human player
		
		for(int i = 0; i < num_players; i++) {
			players[i] = new GameBot(i, num_cards);
		}

		// TODO handle too many players
		//print_deck(game_deck);
		shuffle_deck(game_deck);
		//print_deck(game_deck);

		// Begin game
		for(int round = 0; round < 100; round++) {
			System.out.printf("===============Round %d===============\n", round);
			play_round();
			reveal_hands(players);
			eval_winner();

			// Discard the cards for all of the players
			for(int i = 0; i < num_players; i++) {
				while(players[i].get_hand_size() > 0) {
					discard_deck.place_card(players[i].discard(0));
				}
			}

		}
		//setup_input.close();
		System.out.printf("Thanks for playing! Exiting...\n");

		return;
	}
	private static Deck make_deck() {
		//System.out.println("make_deck()");
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
	private static void play_round() {
			//game_deck.print();
			//discard_deck.print();

			round_init(players);
			//DEBUG_score_eval(players);

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
	}
	
	private static void eval_winner() {
		int winner = -1;
		boolean tie = false;
		boolean first = true;
		int true_score = -1;
		int true_high_card = -1;
		do {
			int max_score = -1;
			// TODO remove this debug text
			if(tie) {
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				reveal_hands(players);
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
				System.out.printf("---------------------HighCard resolution---------------------\n");
			}
			tie = false;
			int max_high_card = -1;
			if(players[0].get_hand_size() == 0)
				break;
			for(int i = 0; i < players.length; i++) {
				int cur_score = 0;
				if(players[i].get_win_type() > 0)
					cur_score = players[i].eval_score();
				if(cur_score > 0) {
					if(max_score < cur_score) {
						max_score = cur_score;
						max_high_card = players[i].get_high_card();
						winner = i;
						tie = false;
					}
					else if(max_score == cur_score) {
						int cur_high_card = players[i].get_high_card();
						// is the high_card the same?
						if(max_high_card == cur_high_card) {
							tie = true;
						}
						// ah new high card
						else if(max_high_card < cur_high_card) {
							max_high_card = players[i].get_high_card();
							winner = i;
							tie = false;
						}
						else {
							// we didn't beat the high card
							players[i].set_is_loser();
							
						}
					}
					else {
						players[i].set_is_loser();
					}
				}
				discard_deck.place_card(players[i].scoring_discard_high_card());
			}
			if(first) {
				true_score = max_score;
				true_high_card = max_high_card;
				first = false;
			}
		} while(tie);
			switch(true_score) {
				case 21:
					System.out.printf("Straight Flush!\n");
					break;
				case 20:
					System.out.printf("Four of a Kind!\n");
					break;
				case 19:
					System.out.printf("Full House\n");
					break;
				case 18:
					System.out.printf("Flush!\n");
					break;
				case 17:
					System.out.printf("Straight!\n");
					break;
				case 16:
					System.out.printf("Three of a Kind!\n");
					break;
				case 15:
					System.out.printf("Two Pair!\n");
					break;
				case 14:
					System.out.printf("One Pair!\n");
					break;
				default:
					System.out.printf("High Card: %c!\n", rank_map[true_high_card]);
					break;
			}

		if(tie) {
			// we can discard the high card and check again(recursive-esque-ly)
			System.out.printf("Tie!\n");
		}
		else {
			System.out.printf("Player %d wins!\n", winner);
		}
		return;
	}
	
	private static void shuffle_deck(Deck game_deck) {
		System.out.println("shuffle_deck()");
		
		game_deck.shuffle_deck(rng);

		return;
	}

	/*
	private static void reveal_hands(GameBot[] players) {
		for(int i = 0; i < players.length; i++) {
			System.out.printf("Player %d's hand':\n", i);
			int score = players[i].eval_score();
			int high_card_val = players[i].get_high_card(); //handles ace high exception
			int high_card_index = high_card_val % 13; //handles ace high exception
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
					System.out.printf("High Card: %c!\n", rank_map[high_card_index]);
					break;
			}
		}
		return;
	}
	*/
	private static void reveal_hands(GameBot[] players) {
		for(int i = 0; i < players.length; i++) {
			System.out.printf("Player %d's hand':\n", i);
			players[i].print_hand();
		}
		return;
	}
	private static void round_init(GameBot[] players) {
		// if we don't have enough cards after the last round
		// add the discard pile to the deck
		if(game_deck.get_num_cards() < players.length * num_cards) {
			// Place the discard deck under the deck
			game_deck.combine(discard_deck);
			// Shuffle the deck 
			//game_deck.shuffle_deck(rng);
		}

		// deal the cards
		for(int i = 0; i < num_cards; i++) {
			Card drawn;
			for(int j = 0; j < players.length; j++) {
				drawn = game_deck.draw_card();
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
	private static void HACK_straight(GameBot[] players, int index) {
		System.out.printf("HACK_test_straight\n");
		for(int i = 0; i < num_cards; i++) {
			players[0].my_hand.get_card(i).set_rank((index+i)%13);
		}
		reveal_hands(players);
		return;
	}
	private static void HACK_flush(GameBot[] players, int index) {
		System.out.printf("HACK_test_flush\n");
		for(int i = 0; i < num_cards; i++) {
			players[0].my_hand.get_card(i).set_suit(index);
		}
		reveal_hands(players);
		return;
	}
	private static void DEBUG_score_eval(GameBot[] players) {
		System.out.printf("DEBUG scoring\n");
		for(int i = 0; i < suit_map.length; i++) {
			HACK_flush(players, i);
		}
		System.out.printf("===================================================\n");
		for(int i = 0; i < rank_map.length; i++) {
			HACK_straight(players, i);
		}
		return;
	}
	//*/

}
