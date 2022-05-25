package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/**
 * This class is used for heal effects. Note: A heal can't increase the power of a card above it's
 * initial power.
 */
public class HealParams extends ActionParams {
  private int healingCardUUID;
  private ArrayList<Integer> healedCardUUIDs;
  private int healPoints;
  private int numTargets;
  private boolean randomTargets;
  private boolean targetsOnlyAlliedUnits;

  /**
   * @param healingCardUUID UUID of the healing card.
   * @param healedCardUUIDs ArrayList containing UUIDs of the healed cards.
   * @param healPoints Amount each target gets healed.
   * @param numTargets Number of targets.
   * @param randomTargets Determines if the targets are chosen at random.
   * @param targetsOnlyAlliedUnits Determines if only allied units can be targeted.
   */
  public HealParams(
      int healingCardUUID,
      ArrayList<Integer> healedCardUUIDs,
      int healPoints,
      int numTargets,
      boolean randomTargets,
      boolean targetsOnlyAlliedUnits) {
    this.healingCardUUID = healingCardUUID;
    this.healedCardUUIDs = healedCardUUIDs;
    this.healPoints = healPoints;
    this.numTargets = numTargets;
    this.randomTargets = randomTargets;
    this.targetsOnlyAlliedUnits = targetsOnlyAlliedUnits;
  }

  public int getHealingCardUUID() {
    return healingCardUUID;
  }

  public ArrayList<Integer> getHealedCardUUIDs() {
    return healedCardUUIDs;
  }

  public int getHealPoints() {
    return healPoints;
  }

  public void setHealedCardUUIDs(ArrayList<Integer> healedCardUUIDs) {
    this.healedCardUUIDs = healedCardUUIDs;
  }

  public void setHealingCardUUID(int healingCardUUID) {
    this.healingCardUUID = healingCardUUID;
  }

  public void setHealPoints(int healPoints) {
    this.healPoints = healPoints;
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
