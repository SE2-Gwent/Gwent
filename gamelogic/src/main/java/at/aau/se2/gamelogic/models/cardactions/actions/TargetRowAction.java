package at.aau.se2.gamelogic.models.cardactions.actions;

import java.util.ArrayList;

public class TargetRowAction {
  private int targetingCardUUID;
  private ArrayList<Integer> targetedRowsUUIDs;
  private final boolean hasRandomTargets;
  private final boolean getsRandomStatus;

  public TargetRowAction(
      int targetingCardUUID,
      ArrayList<Integer> targetedRowsUUIDs,
      boolean hasRandomTargets,
      boolean getsRandomStatus) {
    this.targetingCardUUID = targetingCardUUID;
    this.targetedRowsUUIDs = targetedRowsUUIDs;
    this.hasRandomTargets = hasRandomTargets;
    this.getsRandomStatus = getsRandomStatus;
  }

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
    return hasRandomTargets;
  }

  public boolean getsRandomStatus() {
    return getsRandomStatus;
  }
}
