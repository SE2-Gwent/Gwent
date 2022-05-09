package at.aau.se2.gamelogic.comunication;

public class Result<T, U> {
  public enum Type {
    SUCCESS,
    FAILURE
  }

  private Type type;
  private T value;
  private U error;

  public static <T, U> Result<T, U> Success(T value) {
    return new Result<T, U>(value, null);
  }

  public static <T, U> Result<T, U> Failure(U error) {
    return new Result<T, U>(null, error);
  }

  private Result(T value, U error) {
    this.value = value;
    this.error = error;
    type = error == null ? Type.SUCCESS : Type.FAILURE;
  }

  public T getValue() {
    return value;
  }

  public U getError() {
    return error;
  }

  public Type getType() {
    return type;
  }

  public boolean isSuccessful() {
    return value != null;
  }

  interface Functional<T, U> {
    <V> Result<V, U> apply(T value);
  }

  public <V> Result<V, U> flatMap(Functional<T, U> mapper) {
    if (type == Type.FAILURE) {
      return Result.Failure(error);
    }

    return mapper.apply(value);
  }
}
