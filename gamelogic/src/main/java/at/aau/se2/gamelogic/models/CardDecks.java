package at.aau.se2.gamelogic.models;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Keep;

public class CardDecks {
  private HashMap<String, Card> p1Deck = new HashMap<>();
  private HashMap<String, Card> p2Deck = new HashMap<>();

  @Keep
  public CardDecks() {}

  public CardDecks(ArrayList<Card> p1Deck, ArrayList<Card> p2Deck) {
    setDeck(InitialPlayer.INITIATOR, p1Deck);
    setDeck(InitialPlayer.OPPONENT, p2Deck);
  }

  public Card getCard(int cardId, Player player) {
    switch (player.getInitialPlayerInformation()) {
      case INITIATOR:
        return p1Deck.get(cardId);
      case OPPONENT:
        return p2Deck.get(cardId);
      default:
        return null;
    }
  }

  public void setDeck(InitialPlayer player, ArrayList<Card> cards) {
    HashMap<String, Card> destination = player == InitialPlayer.INITIATOR ? p1Deck : p2Deck;
    for (Card c : cards) {
      destination.put(String.valueOf(c.getId()) + "_card", c);
    }
  }

  public HashMap<String, Card> getP1Deck() {
    return p1Deck;
  }

  public HashMap<String, Card> getP2Deck() {
    return p2Deck;
  }
}
