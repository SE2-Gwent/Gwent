package at.aau.se2.gamelogic.models;

public class Player {
  private int id;
  private int currentMatchPoints;
  private boolean hasPassed;
  private boolean hasLastPlayed;
  private boolean hasLastRoundWon;
  private InitialPlayer initialPlayerInformation;

  public Player(int id, InitialPlayer initialPlayerInformation) {
    this.id = id;
    this.initialPlayerInformation = initialPlayerInformation;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCurrentMatchPoints() {
    return currentMatchPoints;
  }

  public void setCurrentMatchPoints(int currentMatchPoints) {
    this.currentMatchPoints = currentMatchPoints;
  }

  public boolean isHasPassed() {
    return hasPassed;
  }

  public void setHasPassed(boolean hasPassed) {
    this.hasPassed = hasPassed;
  }

  public boolean isHasLastPlayed() {
    return hasLastPlayed;
  }

  public void setHasLastPlayed(boolean hasLastPlayed) {
    this.hasLastPlayed = hasLastPlayed;
  }

  public boolean isHasLastRoundWon() {
    return hasLastRoundWon;
  }

  public void setHasLastRoundWon(boolean hasLastRoundWon) {
    this.hasLastRoundWon = hasLastRoundWon;
  }

  public InitialPlayer getInitialPlayerInformation() {
    return initialPlayerInformation;
  }

  public void setInitialPlayerInformation(InitialPlayer initialPlayerInformation) {
    this.initialPlayerInformation = initialPlayerInformation;
  }
}
