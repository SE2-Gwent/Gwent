package at.aau.se2.gwent;

import at.aau.se2.gamelogic.GameLogic;

public class Environment {
  private static Environment sharedInstance;
  private GameLogic gameLogic;

  private Environment() {
    gameLogic = new GameLogic(null, null);
  }

  public static Environment getSharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new Environment();
    }
    return sharedInstance;
  }

  public GameLogic getGameLogic() {
    return gameLogic;
  }
}
