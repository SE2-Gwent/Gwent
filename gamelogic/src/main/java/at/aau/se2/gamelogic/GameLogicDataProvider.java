package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;

public interface GameLogicDataProvider {
  ArrayList<Card> needsCardDeck();
}
