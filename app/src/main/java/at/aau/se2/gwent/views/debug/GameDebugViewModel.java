package at.aau.se2.gwent.views.debug;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameFieldObserver;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.GameStateCallback;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gwent.Environment;

public class GameDebugViewModel extends ViewModel implements GameStateCallback, GameFieldObserver {
  public MutableLiveData<ViewState> state = new MutableLiveData<>();
  public MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();

  private static final String TAG = GameDebugViewModel.class.getSimpleName();

  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();
  private int mulliganCardId = 1; // does not work correctly, because of random cards

  public GameDebugViewModel() {
    gameLogic.getGameStateMachine().registerListener(this);
    gameLogic.registerGameFieldListener(this);
  }

  public void cancelMulligan() {
    gameLogic.abortMulliganCards();
  }

  public void mulliganCard() {
    gameLogic.mulliganCard(mulliganCardId);
    mulliganCardId++;
  }

  public void roundDone() {
    if (!gameLogic.isMyTurn()) {
      errorMutableLiveData.setValue("Its not your turn");
      return;
    }

    gameLogic.endTurn();
  }

  public void passRound() {
    if (!gameLogic.isMyTurn()) {
      errorMutableLiveData.setValue("Its not your turn");
      return;
    }

    if (!gameLogic.getCurrentPlayerCanPass()) {
      errorMutableLiveData.setValue("You cannot pass");
      return;
    }

    gameLogic.pass();
  }

  @Override
  public void gameStateChanged(@NonNull GameState current, @Nullable GameState old) {
    Log.v(TAG, "State changed to: " + current + " from " + old);
    createCurrentViewState();
  }

  @Override
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);
    createCurrentViewState();
  }

  private void createCurrentViewState() {
    Player current = gameLogic.getGameField().getCurrentPlayer();
    Player opponent = gameLogic.getGameField().getOpponent();

    ViewState newState =
        new ViewState(
            String.valueOf(gameLogic.getGameId()),
            gameLogic.getCurrentGameState(),
            gameLogic.getWhoAmI(),
            gameLogic.getStartingPlayer(),
            gameLogic.getPlayerToTurn(),
            String.valueOf(gameLogic.getGameField().getRoundNumber()),
            (current != null)
                ? gameLogic.getGameField().getPointsForPlayer(current.getInitialPlayerInformation())
                : 0,
            (opponent != null)
                ? gameLogic
                    .getGameField()
                    .getPointsForPlayer(opponent.getInitialPlayerInformation())
                : 0,
            (current != null) ? current.getCurrentMatchPoints() : 0,
            (opponent != null) ? opponent.getCurrentMatchPoints() : 0,
            gameLogic.isMyTurn());
    state.setValue(newState);
  }

  class ViewState {
    private String gameId;
    private String state;
    private String player;
    private String startingPlayer;
    private String playersTurn;
    private String roundNumber;
    private String combinedPlayerPoints;
    private String roundsWon;
    private boolean isMyTurn;

    public ViewState(
        String gameId,
        GameState state,
        @Nullable InitialPlayer player,
        @Nullable InitialPlayer startingPlayer,
        @Nullable InitialPlayer playersTurn,
        String roundNumber,
        int currentPlayerPoints,
        int opponentPlayerPoints,
        int currentPlayerRoundsWon,
        int opponentPlayerRoundsWon,
        boolean isMyTurn) {
      this.gameId = gameId;
      this.state = state.name();
      this.player = player == null ? "Not Set" : "Player: " + player.name();
      this.startingPlayer =
          startingPlayer == null ? "Not Set" : "StartingPlayer: " + startingPlayer.name();
      this.playersTurn = playersTurn == null ? "Not Set" : "PlayersTurn: " + playersTurn.name();
      this.roundNumber = "Round: " + roundNumber;
      this.combinedPlayerPoints =
          "Points: Innitiator " + currentPlayerPoints + ":" + opponentPlayerPoints + " Opponent";
      this.roundsWon =
          "Rounds: Innitiator "
              + currentPlayerRoundsWon
              + ":"
              + opponentPlayerRoundsWon
              + " Opponent";
      this.isMyTurn = isMyTurn;
    }

    public String getGameId() {
      return gameId;
    }

    public String getState() {
      return state;
    }

    public String getPlayer() {
      return player;
    }

    public String getPlayersTurn() {
      return playersTurn;
    }

    public String getStartingPlayer() {
      return startingPlayer;
    }

    public boolean isMyTurn() {
      return isMyTurn;
    }

    public String getRoundNumber() {
      return roundNumber;
    }

    public String getRoundsWon() {
      return roundsWon;
    }

    public String getCombinedPlayerPoints() {
      return combinedPlayerPoints;
    }
  }
}
