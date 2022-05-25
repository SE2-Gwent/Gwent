package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/**
 * This class is used to change the position of a unit with it's opposing row (MELEE -> RANGED and
 * vice versa)
 */
public class SwapParams extends ActionParams {
  private int swappingCardUUID;
  private ArrayList<Integer> swappedCardUUIds;
  private int numTargets;
  private boolean randomTargets;
  private boolean targetsOnlyEnemyUnits;

  /**
   * @param swappingCardUUID UUID of targeting card.
   * @param swappedCardUUIds ArrayList of UUIs of targeted cards.
   * @param numTargets Number of targets.
   * @param randomTargets Determines if the targets are chosen at random.
   * @param targetsOnlyEnemyUnits Determines if it is possible to target allied units.
   */
  public SwapParams(
      int swappingCardUUID,
      ArrayList<Integer> swappedCardUUIds,
      int numTargets,
      boolean randomTargets,
      boolean targetsOnlyEnemyUnits) {
    this.swappingCardUUID = swappingCardUUID;
    this.swappedCardUUIds = swappedCardUUIds;
    this.numTargets = numTargets;
    this.randomTargets = randomTargets;
    this.targetsOnlyEnemyUnits = targetsOnlyEnemyUnits;
  }

  public int getSwappingCardUUID() {
    return swappingCardUUID;
  }

  public ArrayList<Integer> getSwappedCardUUIds() {
    return swappedCardUUIds;
  }

  public void setSwappingCardUUID(int swappingCardUUID) {
    this.swappingCardUUID = swappingCardUUID;
  }

  public void setSwappedCardUUIds(ArrayList<Integer> swappedCardUUIds) {
    this.swappedCardUUIds = swappedCardUUIds;
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

  public boolean isTargetsOnlyEnemyUnits() {
    return targetsOnlyEnemyUnits;
  }

  public void setTargetsOnlyEnemyUnits(boolean targetsOnlyEnemyUnits) {
    this.targetsOnlyEnemyUnits = targetsOnlyEnemyUnits;
  }
}
