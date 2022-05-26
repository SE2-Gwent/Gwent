package at.aau.se2.gwent.views.board;

import java.util.ArrayList;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameFieldObserver;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.GameLogicDataProvider;
import at.aau.se2.gamelogic.GameStateCallback;
import at.aau.se2.gamelogic.comunication.SingleEvent;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gwent.Environment;
import at.aau.se2.gwent.util.DebugHelper;

public class BoardViewModel extends ViewModel
    implements GameFieldObserver, GameLogicDataProvider, GameStateCallback {

  public enum Event {
    SHOW_MULLIGAN
  }

  private static final String TAG = BoardViewModel.class.getSimpleName();
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  private MutableLiveData<BoardViewData> currentState = new MutableLiveData<>();
  private MutableLiveData<SingleEvent<Event>> actionLiveData = new MutableLiveData<>();

  public BoardViewModel() {
    gameLogic.registerGameFieldListener(this);
    gameLogic.setGameLogicDataProvider(this);
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

  public void cancelMulligan() {
    gameLogic.abortMulliganCards();
  }

  // GameFieldObserver
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);
    createCurrentViewState(gameField);
  }

  private void createCurrentViewState(GameField gameField) {
    BoardViewData boardViewState =
        new BoardViewData(gameField, gameLogic.isMyTurn(), gameLogic.getCurrentPlayerCanPass());

    currentState.setValue(boardViewState);
  }

  @Override
  public ArrayList<Card> needsCardDeck() {
    return new ArrayList<>();
  }

  @Override
  public void gameStateChanged(@NonNull GameState current, @Nullable GameState old) {
    Log.v(TAG, "GameState changed: " + current);

    switch (current) {
      case MULLIGAN_CARDS:
        actionLiveData.setValue(new SingleEvent<>(Event.SHOW_MULLIGAN));
      default:
        break;
    }
  }

  }

  // Getters & Setters

  public MutableLiveData<BoardViewData> getCurrentState() {
    return currentState;
  }

  public MutableLiveData<SingleEvent<Event>> getActionLiveData() {
    return actionLiveData;
  }
}
