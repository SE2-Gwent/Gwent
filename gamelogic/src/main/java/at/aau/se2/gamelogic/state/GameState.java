package at.aau.se2.gamelogic.state;

public enum GameState {
  INITIALIZE,
  WAIT_FOR_OPPONENT,
  START_GAME_ROUND,
  DRAW_CARDS,
  MULLIGAN_CARDS,
  START_PLAYER_TURN,
  END_PLAYER_TURN,
  END_ROUND,
  END_GAME
}
