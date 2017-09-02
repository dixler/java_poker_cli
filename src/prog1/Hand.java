package prog1;

public class Hand {
	private Card[] cards;
	private int num_cards;
	private int max_cards;
	

	public Hand(int num_cards) {
		max_cards = num_cards;
		cards = new Card[max_cards];
		num_cards = 0;
	}

	public int get_num_cards() {
		return num_cards;
	}
	public int get_max_cards() {
		return num_cards;
	}
	public void draw(Card my_card) {
		//System.out.printf("drawing: %c of %c\n", my_card.get_rank(), my_card.get_suit());
		cards[num_cards] = my_card;
		num_cards += 1;
		return;
	}
	public Card discard(int index) {
		Card discarded = cards[index];
		num_cards += -1;
		shift_left(index);

		return discarded;
	}
	private void shift_left(int index) {
		for(int i = index; i < num_cards-1; i++) {
			cards[i] = cards[i+1];
		}
		return;
	}
	public int find(int rank, int suit) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_suit() == suit && cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
	public Card peek(int index) {
		//System.out.printf("peeking %d\n", index);
		return cards[index];
	}
}
