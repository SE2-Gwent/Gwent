package at.aau.se2.gamelogic.models.cardactions;

import at.aau.se2.gamelogic.models.Row;

public class FogParams extends ActionParams {
  private final Row row;

  public FogParams(Row row) {
    this.row = row;
  }

  public Row getRow() {
    return row;
  }
}
