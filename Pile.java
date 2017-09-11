import java.util.LinkedList;
import java.util.Random;

public class Pile {
	private LinkedList<Card> card_list = new LinkedList<Card>();
	private int num_cards;

   /*
    * FUNCTION:   public Pile();
    * description:   used to hold the deck and various discard piles
    */
	public Pile() {
		num_cards = 0;
		return;
	}
   /*
    * FUNCTION:   public int get_num_cards();
    * description:   returns the number of cards that are in the player's hand
    */
	public int get_num_cards() {
		return num_cards;
	}
   /*
    * FUNCTION:   public void place_card(Card new_card)();
    * description:   place the new_card at the top of the deck
    */
	public void place_card(Card new_card) {
		card_list.addFirst(new_card);
		num_cards += 1;
		return;
	}
   /*
    * FUNCTION:   public void place_card_bottom(Card new_card)();
    * description:   place the new_card at the bottom of the deck
    */
	public void place_card_bottom(Card new_card) {
		card_list.addLast(new_card);
		num_cards += 1;
		return;
	}
	
   /*
    * FUNCTION:   public Card draw_card();
    * description:   get the first card in the Deck(destructive)
    */
	public Card draw_card() {
		num_cards += -1;
		return card_list.removeFirst();
	}

   /*
    * FUNCTION:   public void combine(Pile child)();
    * description:   moves the cards in the child deck into the parent deck
    */
	public void combine(Pile child) {
		// 
		card_list.addAll(child.card_list);
		// clear old deck

		// handle deck lengths
		num_cards += child.num_cards;
		child.removeAll();
		return;
	}
	// starts from 0
   /*
    * FUNCTION:   public Card extract_ith_card(int index)();
    * description:   returns the ith card from the deck
    */
	public Card extract_ith_card(int index) {
		num_cards += -1;
		return card_list.remove(index);
	}

   /*
    * FUNCTION:   public void shuffle_deck(Random rng)();
    * description:   shuffles the deck
    */
	public void shuffle_deck(Random rng) {
		Pile shuffled = new Pile();

		// extract a random card from the deck then place it into
		// a temporary deck
		for(int i = get_num_cards(); i > 0; --i) {
			int random_index = rng.nextInt(i);
			Card drawn = extract_ith_card(random_index);
			shuffled.place_card(drawn); // for 401 figure out what the shuffling problem's runtime is
		}
		// put the decks together
		combine(shuffled);
		return;
	}

   /*
    * FUNCTION:   public void print(char[] rank_map, char[] suit_map)();
    * description:   print the cards according to the rank_map and suit_map
    */
	public void print(char[] rank_map, char[] suit_map) {
		// draw a card, print the card, then put it at the bottom
		System.out.printf("deck_len: %d\n", num_cards);
		for(int i = 0; i < num_cards; i++) {
			Card drawn = draw_card();
				System.out.printf("%c of %c\n", rank_map[drawn.get_rank()], suit_map[drawn.get_suit()]);
			place_card_bottom(drawn);
		}
		return;
	}
   /*
    * FUNCTION:   public void removeAll();
    * description:   remove all of the Cards from the Pile
    */
	public void removeAll() {
		card_list.clear();
		num_cards = 0;
		return;
	}


}
