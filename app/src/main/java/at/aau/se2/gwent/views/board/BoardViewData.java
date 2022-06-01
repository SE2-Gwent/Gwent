package at.aau.se2.gwent.views.board;

import java.util.HashMap;

import androidx.annotation.StringRes;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gwent.R;

public class BoardViewData implements Cloneable {

  public enum PrimaryButtonMode {
    PASS_ROUND,
    END_ROUND;

    public @StringRes int getText() {
      switch (this) {
        case PASS_ROUND:
          return R.string.pass_round;
        case END_ROUND:
          return R.string.end_round;
      }

      return R.string.done;
    }
  }

  private GameField gameField;
  private boolean myTurn;
  private boolean canPass;
  private String selectedCardId;
  private boolean isGameFieldDirty;

  public BoardViewData(GameField gameField, GameLogic gameLogic) {
    this.gameField = gameField;
    this.myTurn = gameLogic.isMyTurn();
    this.canPass = gameLogic.getCurrentPlayerCanPass();
    isGameFieldDirty = true;
  }

  // gamefield does not get a own cloned object
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public GameField getGameField() {
    return gameField;
  }

  public PrimaryButtonMode getPrimaryButtonMode() {
    if (!myTurn) return PrimaryButtonMode.PASS_ROUND;

    return canPass ? PrimaryButtonMode.PASS_ROUND : PrimaryButtonMode.END_ROUND;
  }

  public boolean isPrimaryButtonEnabled() {
    return myTurn;
  }

  public HashMap<String, Card> getPlayersHandCards() {
    if (gameField == null || gameField.getCurrentPlayer() == null) return null;

    return gameField.getCurrentHandCardsFor(
        gameField.getCurrentPlayer().getInitialPlayerInformation());
  }

  public HashMap<String, Card> getOpponentsHandCards() {
    if (gameField == null || gameField.getOpponent() == null) return null;

    return gameField.getCurrentHandCardsFor(gameField.getOpponent().getInitialPlayerInformation());
  }

  public void setSelectedCardId(String selectedCardId) {
    this.selectedCardId = selectedCardId;
    isGameFieldDirty = false;
  }

  public String getSelectedCardId() {
    return selectedCardId;
  }

  public boolean isGameFieldDirty() {
    return isGameFieldDirty;
  }
}
