package prog1;

public class GameBot {
	Deck my_hand = new Deck();
	private int working;

	public GameBot() {
		working = 1;
	}
	public int is_working() {
		return working;
	}
	public void take_card(Card dealt) {
		my_hand.place_card(dealt);
		return;
	}
	private int bot_logic() {
		/*index
		 * TODO bot logic
		 * 
		 */
		// when done with swaps
		working = 0;
		return -1;
	}
	public Card return_card(int index){
		return my_hand.extract_ith_card(index);
	}
	public Card swap() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		bot_logic();
		Card empty;
		if(working == 1) {
			return return_card(bot_logic());
		}
		return empty;
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		my_hand.print();
		return;
	}

}
