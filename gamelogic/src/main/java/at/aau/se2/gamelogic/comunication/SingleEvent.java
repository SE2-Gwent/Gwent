package at.aau.se2.gamelogic.comunication;

public class SingleEvent<T> {
  private boolean isHandled = false;

  private T value;

  public SingleEvent(T value) {
    this.value = value;
  }

  public T getValueIfNotHandled() {
    if (isHandled) return null;

    isHandled = true;
    return value;
  }

  public T peekValue() {
    return value;
  }
}
