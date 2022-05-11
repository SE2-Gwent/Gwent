package at.aau.se2.gamelogic.util;

import java.util.logging.Level;

public class Log {
  private static final String LoggerName = "GameLogic";

  public static void v(String message) {
    java.util.logging.Logger.getLogger(LoggerName)
        .log(java.util.logging.Level.INFO, "[" + getCallingClassName() + "] " + message);
  }

  public static void i(String message) {
    java.util.logging.Logger.getLogger(LoggerName)
        .log(java.util.logging.Level.INFO, "[" + getCallingClassName() + "] " + message);
  }

  public static void w(String message) {
    java.util.logging.Logger.getLogger(LoggerName)
        .log(Level.WARNING, "[" + getCallingClassName() + "] " + message);
  }

  private static String getCallingClassName() {
    return Thread.currentThread().getStackTrace()[2].getClassName();
  }
}
