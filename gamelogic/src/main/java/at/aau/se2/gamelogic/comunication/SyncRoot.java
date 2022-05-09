package at.aau.se2.gamelogic.comunication;

import java.util.Date;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.GameField;

public class SyncRoot {
  private String createdOn = new Date().toString();
  private GameField gameField;

  @Keep
  public SyncRoot() {}

  public String getCreatedOn() {
    return createdOn;
  }

  public GameField getGameField() {
    return gameField;
  }

  public void setGameField(GameField gameField) {
    this.gameField = gameField;
  }
}
