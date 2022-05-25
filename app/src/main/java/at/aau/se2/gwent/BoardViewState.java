package at.aau.se2.gwent;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;

public class BoardViewState {

  ArrayList<at.aau.se2.gamelogic.models.Card> currentPlayerHandCards = new ArrayList<>();

  public BoardViewState(ArrayList<Card> currentPlayerHandCards) {
    this.currentPlayerHandCards = currentPlayerHandCards;
  }
}
