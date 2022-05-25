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
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gwent.Environment;

public class BoardViewModel extends ViewModel
    implements GameFieldObserver, GameLogicDataProvider, GameStateCallback {
  private static final String TAG = BoardViewModel.class.getSimpleName();
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  private MutableLiveData<BoardViewData> currentState = new MutableLiveData<>();
  private MutableLiveData<SingleEvent<BoardViewData.Event>> actionLiveData =
      new MutableLiveData<>();

  public BoardViewModel() {
    gameLogic.registerGameFieldListener(this);
    gameLogic.setGameLogicDataProvider(this);
    gameLogic.getGameStateMachine().registerListener(this);
  }

  // GameFieldObserver
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);
    createCurrentViewState(gameField);
  }

  private void createCurrentViewState(GameField gameField) {
    GameFieldRows rows = gameField.getRows();
    rows.meleeRowFor(gameField.getCurrentPlayer());
    BoardViewData boardViewState =
        new BoardViewData(gameField.getRows().meleeRowFor(gameField.getCurrentPlayer()));

    /*TODO: Karten die im Gamefield als Handkarten in der Hand anzeigen
    Daten auf die Karten bekomme
    Karten anzeigen View - Daten anzeigen

     */
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
        actionLiveData.setValue(new SingleEvent<>(BoardViewData.Event.SHOW_MULLIGAN));
      default:
        break;
    }
  }

  public MutableLiveData<BoardViewData> getCurrentState() {
    return currentState;
  }

  public MutableLiveData<SingleEvent<BoardViewData.Event>> getActionLiveData() {
    return actionLiveData;
  }
}
