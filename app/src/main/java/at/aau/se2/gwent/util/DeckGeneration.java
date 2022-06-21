package at.aau.se2.gwent.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Application;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.util.json.JsonConverter;

public class DeckGeneration {
  private static final String DECK_DATA = "card-pool.json";
  private static final Integer DECK_SIZE = 25;

  public static ArrayList<Card> generateCardDeck() {
    ArrayList<Card> deck = new ArrayList<>();
    String json = readFileAsString();
    if (json != null) {
      deck = JsonConverter.deserializeCardList(json);
      limitDeck(deck);
      return getRandomDeck(deck);
    } else {
      return deck;
    }
  }

  private static String readFileAsString() {
    String jsonString;
    try {
      InputStream is =
          DeckGeneration.getApplicationUsingReflection()
              .getApplicationContext()
              .getAssets()
              .open(DECK_DATA);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      jsonString = new String(buffer, "UTF-8");
    } catch (Exception e) {
      return null;
    }
    return jsonString;
  }

  private static Application getApplicationUsingReflection() throws Exception {
    return (Application)
        Class.forName("android.app.AppGlobals")
            .getMethod("getInitialApplication")
            .invoke(null, (Object[]) null);
  }

  private static void limitDeck(ArrayList<Card> deck) {
    int count = deck.size();
    while (deck.size() > DECK_SIZE) {
      deck.remove(count - 1);
      count--;
    }
  }

  private static ArrayList<Card> getRandomDeck(ArrayList<Card> deck) {
    Random rand = new Random();
    ArrayList<Card> randomDeck = new ArrayList<>();
    for (int i = 0; i < DECK_SIZE; i++) {
      int randomIndex = rand.nextInt(deck.size());
      randomDeck.add(deck.get(randomIndex));
    }
    return randomDeck;
  }
}
