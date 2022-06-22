package at.aau.se2.gwent.views.board;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameFieldObserver;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.GameStateCallback;
import at.aau.se2.gamelogic.comunication.SingleEvent;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gwent.Environment;

public class BoardViewModel extends ViewModel implements GameFieldObserver, GameStateCallback {

  public enum Event {
    SHOW_MULLIGAN,
    SHOW_WINNER
  }

  private static final String TAG = BoardViewModel.class.getSimpleName();
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  private MutableLiveData<BoardViewData> currentState = new MutableLiveData<>();
  private BoardViewData oldViewData;
  private MutableLiveData<SingleEvent<Event>> actionLiveData = new MutableLiveData<>();

  public BoardViewModel() {
    gameLogic.registerGameFieldListener(this);
    gameLogic.getGameStateMachine().registerListener(this);
  }

  public void didClickPrimaryButton() {
    if (currentState.getValue() == null) return;

    switch (currentState.getValue().getPrimaryButtonMode()) {
      case PASS_ROUND:
        gameLogic.pass();
        break;
      case END_ROUND:
        gameLogic.endTurn();
        break;
    }
  }

  public void didClickHandCard(@NonNull String cardId) {
    if (cardId.equals(getCurrentState().getValue().getSelectedCardId())) return;
    if (!getCurrentState().getValue().isMyTurn()) return;

    BoardViewData newState = null;
    try {
      newState = (BoardViewData) currentState.getValue().clone();
      newState.setSelectedCardId(cardId);
      notifyStateChange(newState);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
  }

  public void didClickRowCard(String cardId) {}

  public void cancelMulligan() {
    gameLogic.abortMulliganCards();
  }

  public void playSelectedCard(RowType rowType, int location) {
    // TODO: change when deploy mechanic is merged

    String cardId = getCurrentState().getValue().getSelectedCardId();
    if (cardId == null) return;

    Card playedCard =
        gameLogic.getGameField().getCurrentHandCards().getCard(cardId, gameLogic.getWhoAmI());
    if (playedCard == null) {
      Log.e(TAG, "Could not find card in players hand.");
      return;
    }

    gameLogic.deployCard(playedCard, rowType, location);

    createCurrentViewState(gameLogic.getGameField());
  }

  // GameFieldObserver
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);
    createCurrentViewState(gameField);
  }

  private void createCurrentViewState(GameField gameField) {
    BoardViewData boardViewState = new BoardViewData(gameField, gameLogic);

    notifyStateChange(boardViewState);
  }

  private void notifyStateChange(BoardViewData newState) {
    oldViewData = currentState.getValue();
    currentState.setValue(newState);
  }

  @Override
  public void gameStateChanged(@NonNull GameState current, @Nullable GameState old) {
    Log.v(TAG, "GameState changed: " + current);

    switch (current) {
      case MULLIGAN_CARDS:
        actionLiveData.setValue(new SingleEvent<>(Event.SHOW_MULLIGAN));
      case END_GAME:
        actionLiveData.setValue(new SingleEvent<>(Event.SHOW_WINNER));
      default:
        break;
    }
  }

  // Getters & Setters
  public MutableLiveData<BoardViewData> getCurrentState() {
    return currentState;
  }

  public MutableLiveData<SingleEvent<Event>> getActionLiveData() {
    return actionLiveData;
  }

  public BoardViewData getOldViewData() {
    return oldViewData;
  }

  // not in viewData, because we need it maybe before last update
  public Boolean haveIWon() {
    Player winner = gameLogic.getWinner();
    if (winner == null) return null;
    return winner.getInitialPlayerInformation() == gameLogic.getWhoAmI();
  }
}
