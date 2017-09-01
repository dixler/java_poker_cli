package prog1;

public class GamePlayer {
	private Deck my_hand = new Deck();
	private boolean working;

	public GamePlayer() {
		working = true;
	}
	public void start_working() { // TODO
		working = true;
		return;
	}
	public void stop_working() { // TODO
		working = false;
		return;
	}
	public boolean is_working() {
		return working;
	}
	public void take_card(Card dealt) {
		my_hand.place_card(dealt);
		return;
	}
	public int hand_size() {
		return my_hand.get_size();
	}
	private int ui() {
		/*
		 * TODO interface
		 */
		// when done with swaps
		if(/* we don't want to swap anymore */ 1 == 1);
			working = false;
		return 0;
	}
	public Card return_card(int index){
		return my_hand.extract_ith_card(index);
	}
	public int discard() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return ui();
	}
	// TODO make more portable
	public boolean hand_contains(char rank, char suit) {
		Card target = new Card(rank, suit);
		return my_hand.contains(target);
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		my_hand.print();
		return;
	}

}
