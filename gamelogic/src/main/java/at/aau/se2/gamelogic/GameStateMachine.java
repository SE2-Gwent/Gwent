package at.aau.se2.gamelogic;

import java.util.ArrayList;

import android.util.Log;
import at.aau.se2.gamelogic.state.GameState;

public class GameStateMachine {
  private static String TAG = GameStateMachine.class.getSimpleName();

  private GameState current = GameState.INITIALIZE;

  private ArrayList<GameStateCallback> listeners = new ArrayList<>();

  public void registerListener(GameStateCallback listener) {
    listeners.add(listener);
    listener.gameStateChanged(current, null);
  }

  public boolean stateEquals(GameState state) {
    return current == state;
  }

  public boolean canProgressTo(GameState state) {
    switch (state) {
      case WAIT_FOR_OPPONENT:
        return current == GameState.INITIALIZE;
      case START_GAME_ROUND:
        return current == GameState.WAIT_FOR_OPPONENT || current == GameState.INITIALIZE;
      case DRAW_CARDS:
        return current == GameState.START_GAME_ROUND || current == GameState.END_ROUND;
      case MULLIGAN_CARDS:
        return current == GameState.DRAW_CARDS;
      case START_PLAYER_TURN:
        return current == GameState.MULLIGAN_CARDS || current == GameState.END_PLAYER_TURN;
      case END_PLAYER_TURN:
        return current == GameState.START_PLAYER_TURN;
      case END_ROUND:
        return current == GameState.END_PLAYER_TURN;
      case END_GAME:
        return current == GameState.END_ROUND;
      default:
        return false;
    }
  }

  public boolean startGame() {
    if (!canProgressTo(GameState.WAIT_FOR_OPPONENT)) {
      Log.w(TAG, "Cannot start game.");
      return false;
    }

    changeState(GameState.WAIT_FOR_OPPONENT);
    return true;
  }

  public boolean joinGame() {
    if (!canProgressTo(GameState.START_GAME_ROUND)) {
      Log.w(TAG, "Cannot join game.");
      return false;
    }

    changeState(GameState.START_GAME_ROUND);
    return true;
  }

  public boolean opponentJoined() {
    if (!canProgressTo(GameState.START_GAME_ROUND)) {
      Log.w(TAG, "Cannot wait for opponent.");
      return false;
    }

    changeState(GameState.START_GAME_ROUND);
    return true;
  }

  public boolean roundCanStart() {
    if (!canProgressTo(GameState.DRAW_CARDS)) {
      Log.w(TAG, "Cannot wait for opponent.");
      return false;
    }

    changeState(GameState.DRAW_CARDS);
    return true;
  }

  public boolean cardsDrawn() {
    if (!canProgressTo(GameState.MULLIGAN_CARDS)) {
      Log.w(TAG, "Cannot draw cards.");
      return false;
    }

    changeState(GameState.MULLIGAN_CARDS);
    return true;
  }

  public boolean cardsChanged() {
    if (!canProgressTo(GameState.START_PLAYER_TURN)) {
      Log.w(TAG, "Cannot start game round.");
      return false;
    }

    changeState(GameState.START_PLAYER_TURN);
    return true;
  }

  public boolean restartTurns() {
    return cardsChanged();
  }

  public boolean restartRound() {
    return roundCanStart();
  }

  public boolean endRound() {
    if (!canProgressTo(GameState.END_ROUND)) {
      Log.w(TAG, "Cannot start game round.");
      return false;
    }

    changeState(GameState.END_ROUND);
    return true;
  }

  public boolean endPlayerTurns() {
    if (!canProgressTo(GameState.END_PLAYER_TURN)) {
      Log.w(TAG, "Cannot start game round.");
      return false;
    }

    changeState(GameState.END_PLAYER_TURN);
    return true;
  }

  public boolean endGame() {
    if (!canProgressTo(GameState.END_GAME)) {
      Log.w(TAG, "Cannot start game round.");
      return false;
    }

    changeState(GameState.END_GAME);
    return true;
  }

  private void changeState(GameState newState) {
    GameState old = current;
    current = newState;
    notifyListeners(old);
  }

  private void notifyListeners(GameState old) {
    for (GameStateCallback listener : listeners) {
      listener.gameStateChanged(current, old);
    }
  }

  public GameState getCurrent() {
    return current;
  }
}
