package prog1;

public class Hand {
	public Card[] cards;
	private int num_cards;
	private int max_cards;
	
	public Hand(int defined_max_cards) {
		max_cards = defined_max_cards;
		cards = new Card[max_cards];
		num_cards = 0;
	}

	public int get_num_cards() {
		return num_cards;
	}
	public int get_max_cards() {
		return max_cards;
	}
	public void draw(Card my_card) {
		//System.out.printf("drawing: %c of %c\n", my_card.get_rank(), my_card.get_suit());
		cards[num_cards] = my_card;
		num_cards += 1;
		return;
	}
	public Card discard(int index) {
		if(index < 0 || index >= cards.length) {
			return null;
		}
		Card discarded = cards[index];
		num_cards += -1;
		shift_left(index);

		return discarded;
	}
	private void shift_left(int index) {
		for(int i = index; i < num_cards; i++) {
			cards[i] = cards[i+1];
		}
		return;
	}
	public int find_first_rank(int rank) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
	public int find(int rank, int suit) {
		for(int i = 0; i < cards.length; i++) {
			if(cards[i].get_suit() == suit && cards[i].get_rank() == rank)
				return i;
		}
		return -1;
	}
	public void print(char[] rank_map, char[] suit_map) {
		for(int i = 0; i < num_cards; i++) {
			System.out.printf("%c%c\n", rank_map[cards[i].get_rank()], suit_map[cards[i].get_suit()]);
		}
		return;
	}
	public int get_rank(int index) {
		return cards[index].get_rank();
	}
	public int get_suit(int index) {
		return cards[index].get_suit();
	}
	public Card get_card(int index) {
		//System.out.printf("peeking %d\n", index);
		return cards[index];
	}
}
