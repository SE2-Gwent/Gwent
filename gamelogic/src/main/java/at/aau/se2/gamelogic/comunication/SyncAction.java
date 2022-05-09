package at.aau.se2.gamelogic.comunication;

import java.util.Objects;

public class SyncAction {
  public enum Type {
    STARTING_PLAYER
  }

  private Type type;
  private String message;

  public SyncAction() {}

  public SyncAction(Type type, String message) {
    this.type = type;
    this.message = message;
  }

  public Type getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SyncAction that = (SyncAction) o;
    return type == that.type && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, message);
  }
}
