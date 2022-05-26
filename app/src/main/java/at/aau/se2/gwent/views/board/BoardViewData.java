package at.aau.se2.gwent.views.board;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;

public class BoardViewData {
  private GameField gameField;

  public BoardViewData(GameField gameField) {
    this.gameField = gameField;
  }

  public GameField getGameField() {
    return gameField;
  }
}
