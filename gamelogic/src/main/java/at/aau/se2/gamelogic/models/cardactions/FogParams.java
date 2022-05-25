package at.aau.se2.gamelogic.models.cardactions;

import at.aau.se2.gamelogic.models.Row;

/** This class is used to implement status effects on rows. */
public class FogParams extends ActionParams {
  private final Row row;
  private boolean randomStatus;

  /**
   * @param row targeted row.
   * @param randomStatus determines if a random status is chosen.
   */
  public FogParams(Row row, boolean randomStatus) {
    this.row = row;
    this.randomStatus = randomStatus;
  }

  public Row getRow() {
    return row;
  }

  public boolean isRandomStatus() {
    return randomStatus;
  }

  public void setRandomStatus(boolean randomStatus) {
    this.randomStatus = randomStatus;
  }
}
