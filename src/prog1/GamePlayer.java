package prog1;

public class GamePlayer {
	Deck my_hand = new Deck();
	

	public GamePlayer() {
		return;
	}
	public void take_card(Card dealt) {
		my_hand.place_card(dealt);
		return;
	}
	public Card return_card(int index){
		return my_hand.extract_ith_card(index);
	}
	public void turn(Deck game_deck) {
		/*
		 * TODO player interface
		 */
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		my_hand.print();
		return;
	}

}
