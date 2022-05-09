package at.aau.se2.gamelogic;

import at.aau.se2.gamelogic.models.GameField;

public interface GameFieldObserver {
  void updateGameField(GameField gameField);
}
