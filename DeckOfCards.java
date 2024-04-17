

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckOfCards {
    private static final Random randomNumbers = new Random();
    private static final int NUMBER_OF_CARDS = 52; // constant # of Cards

    private final List<Card> deck; // Mmn11.Card references

    // an empty deck of cards.

    // constructor fills deck of Cards
    public DeckOfCards() {
        deck = new ArrayList<>(NUMBER_OF_CARDS);
        String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six",
                "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

        // populate deck with Mmn11.Card objects, by using % for the faces, we will go through each face 4 times,and by using / we will go through eac suit 13 times as needed.
        for (int count = 0; count < NUMBER_OF_CARDS; count++) {
            deck.add(new Card(faces[count % 13], suits[count / 13]));
        }
    }

    // shuffle deck of Cards by going through the deck and each time swapping the position.
    public void shuffle() {

        // for each Mmn11.Card, pick another random Mmn11.Card (0-51) and swap them
        for (int first = 0; first < deck.size(); first++) {
            int second = randomNumbers.nextInt(NUMBER_OF_CARDS);

            // swap current Mmn11.Card with randomly selected Mmn11.Card
            Card temp = deck.get(first);
            deck.set(first, deck.get(second));
            deck.set(second, temp);
        }
    }

    // deal one Mmn11.Card
    public Card getCard(int currentCard) {
        return deck.get(currentCard); // return current Mmn11.Card in array
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    public int getSize() {
        return deck.size();
    }

    public void removeCard(int index) {
        deck.remove(index);
    }
}