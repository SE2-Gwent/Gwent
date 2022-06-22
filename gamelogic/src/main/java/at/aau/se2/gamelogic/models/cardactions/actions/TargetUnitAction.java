package at.aau.se2.gamelogic.models.cardactions.actions;

import java.util.ArrayList;

import androidx.annotation.Keep;

/** This class is used to implement actions which target units (damage, heal, boost, swap) */
public class TargetUnitAction {
  /**
   * Type of the action: DAMAGE = Deal damage to targeted units. HEAL = Heal targeted units. Note: a
   * heal can't extend the initial power of the target(s). BOOST = Boost targeted units. Note: a
   * boost can extend the initial power of the target(s). SWAP = Change row (MELEE -> RANGED and
   * vice versa) of targeted units.
   */
  public enum ActionType {
    DAMAGE,
    HEAL,
    BOOST,
    SWAP;
  }

  private int targetingCardUUID;
  private ArrayList<Integer> targetedCardsUUIDs;
  private int points;
  private int numTargets;
  private boolean randomTargets;
  private boolean targetsAlliedUnits;
  private boolean targetsEnemyUnits;
  private ActionType actionType;

  /**
   * @param targetingCardUUID UUID of the card which is targeting.
   * @param targetedCardsUUIDs List of UUIDs of the targeted cards.
   * @param points Number of points which will be applied on the action on each targeted unit.
   * @param numTargets Max number of possible targets.
   * @param randomTargets Determines if the targets are chosen at random or by the player.
   * @param targetsAlliedUnits Determines if it is possible to target allied units.
   * @param targetsEnemyUnits Determines if it is possible to target enemy units.
   * @param actionType Type of the Action (There are currently 4 types: DAMAGE, HEAL, BOOST, SWAP)
   */
  public TargetUnitAction(
      int targetingCardUUID,
      ArrayList<Integer> targetedCardsUUIDs,
      int points,
      int numTargets,
      boolean randomTargets,
      boolean targetsAlliedUnits,
      boolean targetsEnemyUnits,
      ActionType actionType) {
    this.targetingCardUUID = targetingCardUUID;
    this.targetedCardsUUIDs = targetedCardsUUIDs;
    this.points = points;
    this.numTargets = numTargets;
    this.randomTargets = randomTargets;
    this.targetsAlliedUnits = targetsAlliedUnits;
    this.targetsEnemyUnits = targetsEnemyUnits;
    this.actionType = actionType;
  }

  @Keep
  public TargetUnitAction() {}

  public TargetUnitAction(
      int points,
      int numTargets,
      boolean randomTargets,
      boolean targetsAlliedUnits,
      boolean targetsEnemyUnits,
      ActionType actionType) {
    this.points = points;
    this.numTargets = numTargets;
    this.randomTargets = randomTargets;
    this.targetsAlliedUnits = targetsAlliedUnits;
    this.targetsEnemyUnits = targetsEnemyUnits;
    this.actionType = actionType;
  }

  public int getTargetingCardUUID() {
    return targetingCardUUID;
  }

  public ArrayList<Integer> getTargetedCardsUUIDs() {
    return targetedCardsUUIDs;
  }

  public int getPoints() {
    return points;
  }

  public boolean hasRandomTargets() {
    return randomTargets;
  }

  public boolean canTargetAlliedUnits() {
    return targetsAlliedUnits;
  }

  public boolean canTargetEnemyUnits() {
    return targetsEnemyUnits;
  }

  public int getNumTargets() {
    return numTargets;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public void setTargetingCardUUID(int targetingCardUUID) {
    this.targetingCardUUID = targetingCardUUID;
  }

  public void setTargetedCardsUUIDs(ArrayList<Integer> targetedCardsUUIDs) {
    this.targetedCardsUUIDs = targetedCardsUUIDs;
  }
}
