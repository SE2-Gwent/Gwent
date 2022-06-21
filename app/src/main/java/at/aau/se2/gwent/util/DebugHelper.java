package at.aau.se2.gwent.util;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;

public class DebugHelper {

  public static ArrayList<Card> generateTestCards() throws Exception {
    return DeckGeneration.generateCardDeck();
    /*ArrayList<Card> testCards = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      testCards.add(
          new Card(
              i,
              "TestCard " + i,
              new ArrayList<CardType>(),
              i,
              0,
              "This is a test Card",
              null,
              null,
              "rm_ard_feainn_crossbow_man_basic",
              "rm_ard_feainn_crossbow_man_detail"));
    }

    return testCards;*/
  }
}
