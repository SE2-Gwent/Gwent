package at.aau.se2.gamelogic.comunication;

import at.aau.se2.gamelogic.models.GameField;

public interface CommuncationObserver {
  void didUpdateGameField(GameField gameField);
}
