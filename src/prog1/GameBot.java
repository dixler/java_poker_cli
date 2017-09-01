package prog1;

public class GameBot {
	Deck my_hand = new Deck();

	public GameBot() {
	}
	public void take_card(Card dealt) {
		my_hand.place_card(dealt);
		return;
	}
	private int bot_logic() {
		/*
		 * TODO bot logic
		 * 
		 */
		return -1;
	}
	public Card return_card(int index){
		return my_hand.extract_ith_card(index);
	}
	public int turn(Deck game_deck) {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		int index;
		for(;;){
			index = bot_logic();
			if(index != -1) {
				return_card(index);
			}
			else {
				break;
			}
		}
		
		return -1;
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		my_hand.print();
		return;
	}

}
