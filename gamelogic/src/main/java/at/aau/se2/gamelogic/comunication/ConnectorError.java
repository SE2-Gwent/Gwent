package at.aau.se2.gamelogic.comunication;

public class ConnectorError extends Error {
  public ConnectorError(String s) {
    super(s);
  }

  static ConnectorError GameCreationFailed() {
    return new ConnectorError("Could not create game, please try again later.");
  }

  static ConnectorError GameNotFound() {
    return new ConnectorError("Could not find game, please try again later.");
  }
}
