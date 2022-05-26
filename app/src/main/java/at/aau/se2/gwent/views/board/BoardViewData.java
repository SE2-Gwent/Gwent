package at.aau.se2.gwent.views.board;

import java.util.HashMap;

import androidx.annotation.StringRes;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gwent.R;

public class BoardViewData {
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

  public BoardViewData(GameField gameField, boolean myTurn, boolean canPass) {
    this.gameField = gameField;
    this.myTurn = myTurn;
    this.canPass = canPass;
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
    if (gameField.getCurrentPlayer() == null) return null;

    return gameField.getCurrentHandCardsFor(
        gameField.getCurrentPlayer().getInitialPlayerInformation());
  }
}
