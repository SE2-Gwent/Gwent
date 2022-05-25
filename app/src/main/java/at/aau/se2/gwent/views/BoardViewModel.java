package at.aau.se2.gwent.views;

import java.util.ArrayList;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gwent.BoardViewState;

public class BoardViewModel extends ViewModel {

  private MutableLiveData<BoardViewState> currentState = new MutableLiveData<>();
  // String aus der Boardviewmodel heißt.
  private static final String TAG = BoardViewModel.class.getSimpleName();

  // Object das Livestatusupdates gibt (Observer)
  public MutableLiveData<BoardViewState> getCurrentState() {
    return currentState;
  }

  // Handkarten holen
  public ArrayList<Integer> getHandcards() {
    return new ArrayList<>(10);
  }

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
}
