package at.aau.se2.gwent.util;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;

public class DebugHelper {

  public static ArrayList<Card> generateTestCards() throws Exception {
    return DeckGeneration.generateCardDeck();
  }
}
