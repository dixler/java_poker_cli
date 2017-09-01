package prog1;
import java.util.LinkedList;

public class Deck {
	LinkedList<Card> card_list = new LinkedList<Card>();
	int num_cards;

   

	public Deck() {
		num_cards = 0;
		return;
	}
	public void place_card(Card new_card){
		card_list.addFirst(new_card);
		num_cards += 1;
		return;
	}
	public void place_card_bottom(Card new_card){
		card_list.addLast(new_card);
		num_cards += 1;
		return;
	}
	
	// get the first card in the Deck(destructive)
	public Card draw_card(){
		num_cards += -1;
		return card_list.removeFirst();
	}

	// moves the cards in the child deck into the parent deck
	public void combine(Deck child){
		// 
		card_list.addAll(child.card_list);
		// clear old deck
		child.removeAll();

		// handle deck lengths
		num_cards += child.num_cards;
		child.num_cards = 0;
		return;
	}
	public void removeAll(){
		card_list.clear();
		num_cards = 0;
		return;
	}
	//public Card draw_card(int index){
	//}

}
