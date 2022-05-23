package at.aau.se2.gamelogic.models.cardactions;

import at.aau.se2.gamelogic.models.Row;

/*
this class is used for row effects (fog, rain, frost)
it just contains a row on which the status will be applied
 */
public class FogParams extends ActionParams {
  private final Row row;

  public FogParams(Row row) {
    this.row = row;
  }

  public Row getRow() {
    return row;
  }
}
