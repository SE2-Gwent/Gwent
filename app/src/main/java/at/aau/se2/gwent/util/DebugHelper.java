package at.aau.se2.gwent.util;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardType;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;

public class DebugHelper {

  public static ArrayList<Card> generateTestCards() {
    ArrayList<Card> testCards = new ArrayList<>();
    for (int i = 1; i <= 25; i++) {
      testCards.add(
          new Card(
              i,
              "TestCard " + i,
              new ArrayList<CardType>(),
              i,
              0,
              "This is a test Card",
              new ArrayList<ActionParams>()));
    }

    return testCards;
  }
}
