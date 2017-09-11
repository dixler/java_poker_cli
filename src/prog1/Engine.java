package prog1;
import java.util.Random;
import java.util.Scanner;

public class Engine {
	// define the Random Number Generator
		static private Random rng = new Random();
	// various data structures
		static Pile game_deck;
		static Pile discard_deck;
		static Player[] players;
	
	// set game variables
		static int num_cards = 5;
		static int max_discard = 3;

	// maps the raw rank values to the cards
		static char[] suit_map = {	'C', 'D', 'S', 'H'};

	// maps the raw suit values to the cards
		static char[] rank_map = {	'2', '3', '4', '5', 
									'6', '7', '8', '9', 
									'T', 'J', 'Q', 'K', 
									'A'};

	public static void main(String[] args) {

		// Prompt for number of computer players
			Scanner setup_input = new Scanner(System.in);
			System.out.printf("Number of computer players: ");
			int num_bots = setup_input.nextInt();
			int num_players = 1 + num_bots; // have dynamically determined at runtime

		// create the deck
			game_deck = make_deck();
			discard_deck = new Pile();

			System.out.printf("The deck has been shuffled\n\n");
			shuffle_deck(game_deck);

		// check if there are too many players
			if(game_deck.get_num_cards() < num_players * num_cards) {
				System.out.printf("You have too many friends\n");
				setup_input.close();
				return;
			}

		// create player array
			players = new Player[num_players];

			// populate player array
			//players[0] = new User(0, num_cards);
			for(int i = 0; i < num_players; i++) {
				players[i] = new Player(i, num_cards);
			}

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
			/*
			game_deck.combine(discard_deck);
			game_deck.print(rank_map, suit_map);
			*/

		System.out.printf("Thanks for playing! Exiting...\n");

		// tidy up
			setup_input.close();
			return;
	}
	
	/*
	 * Initialize the deck with the rank_map's and suit_map's
	 */
	private static Pile make_deck() {
		Pile game_deck = new Pile();

		// create a card and then put it into the deck
		for(int suit = 0; suit < suit_map.length; suit++) {
			for(int rank = 0; rank < rank_map.length; rank++) {
				Card my_card = new Card(rank, suit);
				game_deck.place_card(my_card);
			}
		}
		return game_deck;
	}

	// plays the round. The player has an override that allows for manual control of the turn
	private static void play_round() {
		round_init(players);
		// For each player allow them to discard
		for(int i = 0; i < players.length; i++) {
			Pile discard_pile;

			discard_pile = players[i].turn(max_discard + ace_exception(players[i]));
			int num_discarded = discard_pile.get_num_cards();

			discard_deck.combine(discard_pile);

			if(i != 0) System.out.printf("Player %d discarded %d cards\n", players[i].get_player_id(), num_discarded);
			else System.out.printf("You discarded %d cards\n", num_discarded);
			
			// deal cards to player until hand is full again
			while(players[i].get_hand_size() < num_cards) {
				// if we run out of cards somehow, we 
				// shuffle the discard deck and use it

				if(game_deck.get_num_cards() == 0) {
					// Shuffle the discard pile
					shuffle_deck(discard_deck);
					// Place the shuffled discard pile under the deck
					game_deck.combine(discard_deck);
					System.out.printf("[NOTE] The discarded cards have been shuffled \n"
									+ "and have replenished the deck\n");
				}
				// deal a card from the deck
				players[i].draw_card(game_deck.draw_card());
				
			}
		}
	}
	
	// after everyone has finished swapping cards, evaluate the winner
	// works by evaluating who has the highest ranking hand
	// if there's a tie, we discard the high card and then reevaluate the score
	// it works because if two people have the same tie, discarding 
	// one card forces the evaluator to check the next best thing.
	private static void eval_winner() {
		int winner = -1;
		// used in order to catch ties
			boolean tie = false;

		// catches the highest win combination 
			boolean first = true;
			int true_score = -1;
			int true_high_card = -1;

		// conflict resolution loop
		// discards and then compares hand score
			do {
				if(players[0].get_hand_size() == 0)
					break;
				int max_score = -1;
				tie = false;
				int max_high_card = -1;
				for(int i = 0; i < players.length; i++) {
					int cur_score = 0;
					if(players[i].get_player_score() > 0) {
						cur_score = players[i].eval_score();

						if(cur_score > 0) {
							if(max_score < cur_score) {
								max_score = cur_score;
								max_high_card = players[i].get_high_card();
								winner = i;
							}
							else if(max_score == cur_score) {
								int cur_high_card = players[i].get_high_card();
								// is the high_card the same?
								if(max_high_card == cur_high_card) {
									// tie is only modified here to true 
									// if there is actually a tie now after 
									// removing cards
									tie = true;
								}
								// ah new high card
								else if(max_high_card < cur_high_card) {
									max_high_card = players[i].get_high_card();
									winner = i;
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
					}
					discard_deck.place_card(players[i].scoring_discard_high_card());
				}
			// catch true win(flush, straight, etc.)
				if(first) {
					true_score = max_score;
					true_high_card = max_high_card;
					first = false;
				}
			} while(tie);
		
		
		// print out round result
		if(tie) {
			System.out.printf("Tie!\n");
		}
		else {
			if(winner != 0) System.out.printf("Player %d wins with a ", winner);
			else System.out.printf("You win with a ");
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
		}
		return;
	}
	
	// shuffles the deck
	private static void shuffle_deck(Pile game_deck) {
		game_deck.shuffle_deck(rng);
		return;
	}

	// display everyone's hands
	private static void reveal_hands(Player[] players) {
		for(int i = 0; i < players.length; i++) {
			players[i].eval_score();
			if(i != 0) System.out.printf("Player %d's hand':\n", i);
			else System.out.printf("Your hand':\n");
			players[i].print_hand();
			switch(players[i].get_player_score()) {
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
					System.out.printf("High Card: %c!\n", rank_map[players[i].get_high_card()]);
					break;
			}
		}
		return;
	}

	// prepare the game_deck for the next round if we don't have enough cards
	private static void round_init(Player[] players) {
		// if we don't have enough cards after the last round
		// add the discard pile to the deck
		if(game_deck.get_num_cards() < players.length * num_cards) {
			// Shuffle the deck 
			shuffle_deck(discard_deck);
			// Place the discard deck under the deck
			game_deck.combine(discard_deck);
			System.out.printf("[NOTE] The discarded cards have been shuffled \n"
							+ "and placed under the deck\n\n");
		}

		// deal the cards
		System.out.printf("Dealing the cards\n");
		for(int i = 0; i < num_cards; i++) {
			Card drawn;
			for(int j = 0; j < players.length; j++) {
				drawn = game_deck.draw_card();
				players[j].draw_card(drawn);
			}
		}
	}

	//	does the player have an ace
	//	(returns an integer so we can add this to the number of swaps)
	private static int ace_exception(Player cur_player) {
		if(cur_player.count_by_rank('A') > 0) {
			return 1;
		}
		return 0;
	}
}
