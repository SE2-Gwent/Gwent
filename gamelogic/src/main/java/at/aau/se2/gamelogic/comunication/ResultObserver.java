package at.aau.se2.gamelogic.comunication;

public interface ResultObserver<T, U> {
  void finished(Result<T, U> result);
}
