package at.aau.se2.communication;

public interface ResultObserver<T, U> {
  void finished(Result<T, U> result);
}
