package at.aau.se2.gwent.views.board;

import java.util.HashMap;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.state.GameState;
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

  public enum HeroView {
    GERALD,
    TRISS;

    public @DrawableRes int backgroundDrawable() {
      return this == GERALD
          ? R.drawable.button_round_hero_gerald_background
          : R.drawable.button_round_hero_triss_background;
    }

    public HeroView other() {
      return this == GERALD ? TRISS : GERALD;
    }
  }

  private GameField gameField;
  private boolean myTurn;
  private boolean canPass;
  private InitialPlayer whoAmI;
  private String selectedCardId;
  private boolean isGameFieldDirty;
  private String roundNumber;
  private String currentPlayersPoints;
  private String opponentPoints;
  private int currentPlayerRoundsWon;
  private int opponentRoundsWon;
  private GameState currentGameState;
  private String gameId;
  private boolean heroEnabled;
  private HeroView myHeroView;

  public BoardViewData(@NonNull GameField gameField, @NonNull GameLogic gameLogic) {
    Player currentPlayer = gameField.getPlayer(gameLogic.getWhoAmI());
    Player opponent = gameField.getPlayer(gameLogic.getWhoAmI().other());

    this.gameField = gameField;
    this.whoAmI = gameLogic.getWhoAmI();
    this.myTurn = gameLogic.isMyTurn();
    this.canPass = gameLogic.getCurrentPlayerCanPass();
    this.roundNumber =
        String.valueOf(
            gameLogic.getGameField().getRoundNumber() + 1); // we start counting with round 0
    this.currentPlayersPoints = String.valueOf(gameField.getPointsForPlayer(gameLogic.getWhoAmI()));
    this.opponentPoints =
        String.valueOf(gameField.getPointsForPlayer(gameLogic.getWhoAmI().other()));
    this.currentPlayerRoundsWon =
        (currentPlayer != null) ? currentPlayer.getCurrentMatchPoints() : 0;
    this.opponentRoundsWon = (opponent != null) ? opponent.getCurrentMatchPoints() : 0;
    this.currentGameState = gameLogic.getCurrentGameState();
    this.gameId = String.valueOf(gameLogic.getGameId());
    this.heroEnabled = gameField.canHeroUseAction(whoAmI);
    this.myHeroView = whoAmI == InitialPlayer.INITIATOR ? HeroView.GERALD : HeroView.TRISS;

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

    return gameField.getCurrentHandCardsFor(whoAmI);
  }

  public HashMap<String, Card> getOpponentsHandCards() {
    if (gameField == null || gameField.getOpponent() == null) return null;

    return gameField.getCurrentHandCardsFor(whoAmI.other());
  }

  public void setSelectedCardId(String selectedCardId) {
    this.selectedCardId = selectedCardId;
    isGameFieldDirty = false;
  }

  public String getSelectedCardId() {
    return selectedCardId;
  }

  public InitialPlayer getWhoAmI() {
    return whoAmI;
  }

  public boolean isGameFieldDirty() {
    return isGameFieldDirty;
  }

  public boolean isMyTurn() {
    return myTurn;
  }

  public String getRoundNumber() {
    return roundNumber;
  }

  public String getCurrentPlayersPoints() {
    return currentPlayersPoints;
  }

  public String getOpponentPoints() {
    return opponentPoints;
  }

  public int getCurrentPlayerRoundsWon() {
    return currentPlayerRoundsWon;
  }

  public int getOpponentRoundsWon() {
    return opponentRoundsWon;
  }

  public GameState getCurrentGameState() {
    return currentGameState;
  }

  public boolean isHeroEnabled() {
    return heroEnabled && myTurn;
  }

  public String getGameId() {
    return gameId;
  }

  public HeroView getMyHeroView() {
    return myHeroView;
  }
}
