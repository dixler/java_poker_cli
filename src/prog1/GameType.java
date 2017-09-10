package prog1;
import java.util.Random;
import java.util.Scanner;

public class GameType {
	static private Random rng = new Random();
	static int num_cards = 5;
	static int max_discard = 3;

	static Pile game_deck;
	static Pile discard_deck;
	static Player[] players;
	

	static char[] suit_map = {	'C', 'D', 'S', 'H'};

	static char[] rank_map = {	'2', '3', '4', '5', 
								'6', '7', '8', '9', 
								'T', 'J', 'Q', 'K', 
								'A'};

	public static void main(String[] args) {
		//*
		Scanner setup_input = new Scanner(System.in);
		System.out.printf("Number of computer players: ");
		int num_bots = setup_input .nextInt();
		int num_players = 1 + num_bots; // have dynamically determined at runtime
		/* TEMPORARY
		int num_players = 4; // have dynamically determined at runtime
		//*/


		// create the deck
		game_deck = make_deck();
		discard_deck = new Pile();

		// check if there are too many players
		if(game_deck.get_num_cards() < num_players * num_cards) {
			System.out.printf("You have too many friends\n");
			setup_input.close();
			return;
		}

		// create player array
		players = new Player[num_players];

		//players[0] = new GamePlayer(0, num_cards);
		// TODO remember to add human player
		for(int i = 0; i < num_players; i++) {
			players[i] = new Player(i, num_cards);
		}

		shuffle_deck(game_deck);

		// Begin game
		for(int round = 0; round < 1; round++) {
			System.out.printf("===============Round %d===============\n", round);
			play_round();
			custom_hand_STRAIGHT_TIE();
			reveal_hands(players);
			eval_winner();

			// Discard the cards for all of the players
			for(int i = 0; i < num_players; i++) {
				while(players[i].get_hand_size() > 0) {
					discard_deck.place_card(players[i].discard(0));
				}
			}
		}
		setup_input.close();
		System.out.printf("Thanks for playing! Exiting...\n");

		return;
	}
	
	private static void custom_hand_STRAIGHT_TIE() {
		for(int i = 0; i < players.length; i++) {
			//*
			// straight
			players[i].my_hand.cards[0].set_rank(12);
			players[i].my_hand.cards[1].set_rank(0);
			players[i].my_hand.cards[2].set_rank(1);
			players[i].my_hand.cards[3].set_rank(2);
			players[i].my_hand.cards[4].set_rank(3);
			/*
			// random flush
			players[i].my_hand.cards[0].set_rank(2);
			players[i].my_hand.cards[0].set_suit(2);

			players[i].my_hand.cards[1].set_rank(0);
			players[i].my_hand.cards[1].set_suit(2);

			players[i].my_hand.cards[2].set_rank(1);
			players[i].my_hand.cards[2].set_suit(1);

			players[i].my_hand.cards[3].set_rank(6);
			players[i].my_hand.cards[3].set_suit(2);
			
			players[i].my_hand.cards[4].set_rank(3);
			players[i].my_hand.cards[4].set_suit(2);
			//*/
		}
		return;
	}
	
	
	private static Pile make_deck() {
		//System.out.println("make_deck()");
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
	private static void play_round() {
			round_init(players);

			// For each player allow them to discard
			for(int i = 0; i < players.length; i++) {
				Pile discard_pile;

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
			if(players[0].get_hand_size() == 0)
				break;
			int max_score = -1;
			tie = false;
			int max_high_card = -1;
			for(int i = 0; i < players.length; i++) {
				int cur_score = 0;
				if(players[i].get_win_type() > 0) {
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
			if(first) {
				true_score = max_score;
				true_high_card = max_high_card;
				first = false;
			}
		} while(tie);
			if(tie) {
				// we can discard the high card and check again(recursive-esque-ly)
				System.out.printf("Tie!\n");
			}
			else {
				System.out.printf("Player %d wins!\n", winner);
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
	
	private static void shuffle_deck(Pile game_deck) {
		System.out.println("shuffle_deck()");
		
		game_deck.shuffle_deck(rng);

		return;
	}

	private static void reveal_hands(Player[] players) {
		for(int i = 0; i < players.length; i++) {
			System.out.printf("Player %d's hand':\n", i);
			players[i].print_hand();
		}
		return;
	}
	private static void round_init(Player[] players) {
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
	private static int ace_exception(Player players) {
		if(players.count_by_rank('A') > 0) {
			return 1;
		}
		return 0;

	}
}
