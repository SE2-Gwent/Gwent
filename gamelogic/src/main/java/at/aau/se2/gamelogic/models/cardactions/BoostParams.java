package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/**
 * This class is used for boost effects. Note: A boost increase the power of a card above it's
 * initial power.
 */
public class BoostParams extends ActionParams {
  private int boostingCardUUID;
  private ArrayList<Integer> boostedCardUUIDs;
  private int boostPoints;
  private int numTargets;
  private boolean randomTargets;
  private boolean targetsOnlyAlliedUnits;

  /**
   * @param boostingCardUUID UUID of the boosting card.
   * @param boostedCardUUIDs ArrayList containing UUIDs of boosted cards.
   * @param boostPoints Amount of boost for each target.
   * @param numTargets Number of targets.
   * @param randomTargets Determines if a random target is chosen.
   * @param targetsOnlyAlliedUnits Determines if it is possible to target enemy units too.
   */
  public BoostParams(
      int boostingCardUUID,
      ArrayList<Integer> boostedCardUUIDs,
      int boostPoints,
      int numTargets,
      boolean randomTargets,
      boolean targetsOnlyAlliedUnits) {
    this.boostingCardUUID = boostingCardUUID;
    this.boostedCardUUIDs = boostedCardUUIDs;
    this.boostPoints = boostPoints;
    this.numTargets = numTargets;
    this.randomTargets = randomTargets;
    this.targetsOnlyAlliedUnits = targetsOnlyAlliedUnits;
  }

  public int getBoostingCardUUID() {
    return boostingCardUUID;
  }

  public ArrayList<Integer> getBoostedCardUUIDs() {
    return boostedCardUUIDs;
  }

  public void setBoostingCardUUID(int boostingCardUUID) {
    this.boostingCardUUID = boostingCardUUID;
  }

  public void setBoostedCardUUIDs(ArrayList<Integer> boostedCardUUIDs) {
    this.boostedCardUUIDs = boostedCardUUIDs;
  }

  public int getBoostPoints() {
    return boostPoints;
  }

  public void setBoostPoints(int boostPoints) {
    this.boostPoints = boostPoints;
  }

  public int getNumTargets() {
    return numTargets;
  }

  public void setNumTargets(int numTargets) {
    this.numTargets = numTargets;
  }

  public boolean isRandomTargets() {
    return randomTargets;
  }

  public void setRandomTargets(boolean randomTargets) {
    this.randomTargets = randomTargets;
  }

  public boolean isTargetsOnlyAlliedUnits() {
    return targetsOnlyAlliedUnits;
  }

  public void setTargetsOnlyAlliedUnits(boolean targetsOnlyAlliedUnits) {
    this.targetsOnlyAlliedUnits = targetsOnlyAlliedUnits;
  }
}
