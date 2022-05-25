package at.aau.se2.gwent.views.board;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;

public class BoardViewData {
  public enum Event {
    SHOW_MULLIGAN
  }

  private ArrayList<at.aau.se2.gamelogic.models.Card> currentPlayerHandCards;
  private ArrayList<at.aau.se2.gamelogic.models.Card> opponentHandCards;

  public BoardViewData(ArrayList<Card> currentPlayerHandCards) {
    this.currentPlayerHandCards = currentPlayerHandCards;
  }

  public ArrayList<Card> getCurrentPlayerHandCards() {
    return currentPlayerHandCards;
  }

  public ArrayList<Card> getOpponentHandCards() {
    return opponentHandCards;
  }
}
