package prog1;

public class GameBot {
	private Deck my_hand = new Deck();
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
	public int hand_size() {
		return my_hand.get_size();
	}
	private int bot_logic() {
		/*index
		 * TODO bot logic
		 * 
		 */
		if(/* we don't want to swap anymore */ 1 == 1);
			working = 0;
		return 0;
	}
	public Card return_card(int index){
		return my_hand.extract_ith_card(index);
	}
	public int swap() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return bot_logic();
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		my_hand.print();
		return;
	}

}
