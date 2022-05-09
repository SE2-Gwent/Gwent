package at.aau.se2.gwent;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameFieldObserver;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.GameStateCallback;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.state.GameState;

public class GameDebugViewModel extends ViewModel implements GameStateCallback, GameFieldObserver {
  public MutableLiveData<ViewState> state = new MutableLiveData<>();

  private static final String TAG = GameDebugViewModel.class.getSimpleName();

  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  public GameDebugViewModel() {
    gameLogic.getGameStateMachine().registerListener(this);
    gameLogic.registerGameFieldListener(this);
  }

  @Override
  public void gameStateChanged(@NonNull GameState current, @Nullable GameState old) {
    Log.v(TAG, "State changed to: " + current + " from " + old);
  }

  @Override
  public void updateGameField(GameField gameField) {
    Log.v(TAG, "Update Gamefield: " + gameField);

    ViewState newState =
        new ViewState(
            String.valueOf(gameLogic.getGameId()),
            gameLogic.getCurrentGameState().name(),
            gameLogic.getWhoAmI().name());
    state.setValue(newState);
  }

  class ViewState {
    private String gameId;
    private String state;
    private String player;

    public ViewState(String gameId, String state, String player) {
      this.gameId = gameId;
      this.state = state;
      this.player = player;
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
  }
}
