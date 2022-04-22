package at.aau.se2.gamelogic.models.cardactions;

import at.aau.se2.gamelogic.models.Row;

public class DeployParams extends ActionParams {
  private final int cardUUID;
  private final Row row;
  private final int position;

  public DeployParams(int cardUUID, Row row, int position) {
    this.cardUUID = cardUUID;
    this.row = row;
    this.position = position;
  }

  public int getCardUUID() {
    return cardUUID;
  }

  public Row getRow() {
    return row;
  }

  public int getPosition() {
    return position;
  }
}
