package at.aau.se2.gwent.views.board;

import java.util.ArrayList;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import at.aau.se2.gamelogic.GameFieldObserver;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gwent.Environment;

public class BoardViewModel extends ViewModel implements GameFieldObserver {
  private static final String TAG = BoardViewModel.class.getSimpleName();
  private MutableLiveData<BoardViewState> currentState = new MutableLiveData<>();
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  public BoardViewModel() {
    gameLogic.registerGameFieldListener(this);
  }

  // GameFieldObserver
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);
    createCurrentViewState(gameField);
  }

  private void createCurrentViewState(GameField gameField) {

    // currentPlayerHandcard
    GameFieldRows rows = gameField.getRows();
    rows.meleeRowFor(gameField.getCurrentPlayer());
    BoardViewState boardViewState =
        new BoardViewState(gameField.getRows().meleeRowFor(gameField.getCurrentPlayer()));

    /*TODO: Karten die im Gamefield als Handkarten in der Hand anzeigen
    Daten auf die Karten bekomme
    Karten anzeigen View - Daten anzeigen

     */
    currentState.setValue(boardViewState);
  }

  public MutableLiveData<BoardViewState> getCurrentState() {
    return currentState;
  }
}
