package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gamelogic.util.Log;

public class GameStateMachine {
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
      case START_GAME_ROUND:
        return current == GameState.INITIALIZE;
      case DRAW_CARDS:
        return current == GameState.START_GAME_ROUND;
      default:
        return false;
    }
  }

  public boolean startGame() {
    if (!canProgressTo(GameState.START_GAME_ROUND)) {
      Log.w("Cannot start game.");
      return false;
    }

    changeState(GameState.START_GAME_ROUND);
    return true;
  }

  public boolean joinGame() {
    if (!canProgressTo(GameState.START_GAME_ROUND)) {
      Log.w("Cannot join game.");
      return false;
    }

    changeState(GameState.START_GAME_ROUND);
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