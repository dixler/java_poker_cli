package prog1;

public class GameBot extends GamePlayer{
	private int bot_logic() {
		int id_discard = -1;
		// TODO determine optimal card to swap out
		if(id_discard >= 0) {
			
		}
		else{
			// when done with swaps
			stop_working();
		}
		return id_discard;
	}
	public int discard() {
		// This is kinda dumb since we can just have a GamePlayer class manage two different
		// methods of interacting with the game without the need of a separate class
		return bot_logic();
	}
}
