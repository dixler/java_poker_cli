package prog1;

public class GamePlayer {
	private Hand my_hand;
	private boolean working;
	private int player_id;

	public GamePlayer(int id, int num_cards) {
		my_hand = new Hand(num_cards);
		working = true;
		player_id = id;
		return;
	}
	public int player_id() {
		return player_id;
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
	public void draw_card(Card dealt) {
		my_hand.draw(dealt);
		return;
	}
	public int hand_size() {
		return my_hand.get_num_cards();
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
	public Card discard(int index){
		return my_hand.discard(index);
	}
	public int turn() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return ui();
	}
	// TODO make more portable
	public boolean is_holding(char rank, char suit) {
		return my_hand.find(rank, suit) != -1;
	}
	// TODO REMOVE DEBUG
	public void print_hand() {
		// draw a card, print the card, then put it at the bottom
		for(int i = 0; i < my_hand.get_max_cards(); i++) {
			System.out.printf("%c of %c\n", my_hand.peek(i).get_rank(), my_hand.peek(i).get_suit());
		}
		return;
		//my_hand.print();
	}

}
