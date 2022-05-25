package at.aau.se2.gamelogic.models;

public enum InitialPlayer {
  // The player who starts a game
  INITIATOR,
  // The player who joins a game
  OPPONENT;

  public InitialPlayer other() {
    return (this == INITIATOR) ? OPPONENT : INITIATOR;
  }
}
