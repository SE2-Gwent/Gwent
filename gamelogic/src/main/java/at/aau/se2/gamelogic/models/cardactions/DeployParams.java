package at.aau.se2.gamelogic.models.cardactions;

import at.aau.se2.gamelogic.models.Row;

public class DeployParams extends ActionParams {
  private final int cardUUID;
  private final Row row;

  public DeployParams(int cardUUID, Row row) {
    this.cardUUID = cardUUID;
    this.row = row;
  }

  public int getCardUUID() {
    return cardUUID;
  }

  public Row getRow() {
    return row;
  }
}
