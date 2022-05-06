package at.aau.se2.communication;

public class ConnectorError extends Error {
  public ConnectorError(String s) {
    super(s);
  }

  static ConnectorError GameCreationFailed() {
    return new ConnectorError("Could not create game, please try again later.");
  }
}
