package at.aau.se2.gamelogic.models.cardactions.actions;

import java.util.ArrayList;

import androidx.annotation.Keep;

/** this class is used to implement actions which target rows (e.g. frost, rain, fog). */
public class TargetRowAction {
  private int targetingCardUUID;
  private ArrayList<Integer> targetedRowsUUIDs;
  private boolean randomTargets;
  private boolean randomStatus;

  /**
   * @param targetingCardUUID UUID of the card which is targeting.
   * @param targetedRowsUUIDs List of UUIDS of the targeted rows.
   * @param randomTargets Determines if the targeted rows are chosen at random.
   * @param randomStatus Determines if the applied status is chosen at random.
   */
  public TargetRowAction(
      int targetingCardUUID,
      ArrayList<Integer> targetedRowsUUIDs,
      boolean randomTargets,
      boolean randomStatus) {
    this.targetingCardUUID = targetingCardUUID;
    this.targetedRowsUUIDs = targetedRowsUUIDs;
    this.randomTargets = randomTargets;
    this.randomStatus = randomStatus;
  }

  @Keep
  public TargetRowAction() {}

  public int getTargetingCardUUID() {
    return targetingCardUUID;
  }

  public void setTargetingCardUUID(int targetingCardUUID) {
    this.targetingCardUUID = targetingCardUUID;
  }

  public ArrayList<Integer> getTargetedRowsUUIDs() {
    return targetedRowsUUIDs;
  }

  public void setTargetedRowsUUIDs(ArrayList<Integer> targetedRowsUUIDs) {
    this.targetedRowsUUIDs = targetedRowsUUIDs;
  }

  public boolean hasRandomTargets() {
    return randomTargets;
  }

  public boolean hasRandomStatus() {
    return randomStatus;
  }
}
