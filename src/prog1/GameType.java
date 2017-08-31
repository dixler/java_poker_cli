package prog1;

public class GameType {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int suit = 0; suit < 4; suit++)
		for(int rank = 0; rank < 14; rank++) {
			Card my_card = new Card(rank, suit);
			System.out.printf("card: suit: %d rank: %d\n", my_card.get_suit(), my_card.get_rank());
		}


	}
	private static make_deck() {
		
		return
	}

}
