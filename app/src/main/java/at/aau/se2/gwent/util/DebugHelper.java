package at.aau.se2.gwent.util;

import java.util.ArrayList;

import android.content.Context;
import at.aau.se2.gamelogic.models.Card;

public class DebugHelper {

  public static ArrayList<Card> generateTestCards(Context applicationContext) throws Exception {
    return DeckGeneration.generateCardDeck(applicationContext);
  }
}
