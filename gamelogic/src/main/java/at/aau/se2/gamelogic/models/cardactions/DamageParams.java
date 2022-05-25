package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/** This class is used for damage effects. */
public class DamageParams extends ActionParams {
  private int attackingCardUUID;
  private ArrayList<Integer> attackedCardUUIDs;
  private int damagePoints;
  private int numTargets;
  private boolean randomTargets;
  private boolean targetsOnlyEnemyUnits;

  /**
   * @param attackingCardUUID UUID of the attacking card.
   * @param attackedCardUUIDs ArrayList containing UUIDS of attacked cards.
   * @param damagePoints Amount of damage dealt to one target.
   * @param nTargets Number of target units.
   * @param randomTargets Determines if the targets are chosen at random.
   * @param targetsOnlyEnemyUnits Determines if it is possible to target allied units.
   */
  public DamageParams(
      int attackingCardUUID,
      ArrayList<Integer> attackedCardUUIDs,
      int damagePoints,
      int nTargets,
      boolean randomTargets,
      boolean targetsOnlyEnemyUnits) {
    this.attackingCardUUID = attackingCardUUID;
    this.attackedCardUUIDs = attackedCardUUIDs;
    this.damagePoints = damagePoints;
    this.numTargets = nTargets;
    this.randomTargets = randomTargets;
    this.targetsOnlyEnemyUnits = targetsOnlyEnemyUnits;
  }

  public int getAttackingCardUUID() {
    return attackingCardUUID;
  }

  public ArrayList<Integer> getAttackedCardUUIDs() {
    return attackedCardUUIDs;
  }

  public int getDamagePoints() {
    return damagePoints;
  }

  public void setAttackingCardUUID(int attackingCardUUID) {
    this.attackingCardUUID = attackingCardUUID;
  }

  public void setAttackedCardUUIDs(ArrayList<Integer> attackedCardUUIDs) {
    this.attackedCardUUIDs = attackedCardUUIDs;
  }

  public void setDamagePoints(int damagePoints) {
    this.damagePoints = damagePoints;
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
