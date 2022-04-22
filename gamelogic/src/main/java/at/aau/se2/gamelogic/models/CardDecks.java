package at.aau.se2.gamelogic.models;

import java.util.ArrayList;
import java.util.HashMap;

public class CardDecks {
  private HashMap<Integer, Card> p1Deck = new HashMap<>();
  private HashMap<Integer, Card> p2Deck = new HashMap<>();

  public CardDecks(ArrayList<Card> p1Deck, ArrayList<Card> p2Deck) {
    for (Card c : p1Deck) this.p1Deck.put(c.getId(), c);

    for (Card c : p2Deck) this.p2Deck.put(c.getId(), c);
  }

  public Card getCard(int cardId, Player player) {
    switch (player) {
      case ME:
        return p1Deck.get(cardId);
      case OPPONENT:
        return p2Deck.get(cardId);
      default:
        return null;
    }
  }
}
