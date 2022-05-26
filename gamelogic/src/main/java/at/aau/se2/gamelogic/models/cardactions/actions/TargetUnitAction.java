package at.aau.se2.gamelogic.models.cardactions.actions;

import java.util.ArrayList;

/** This class is used to implement actions which target units (damage, heal, boost, swap) */
public class TargetUnitAction implements CardAction {
  /**
   * Type of the action: DAMAGE = Deal damage to targeted units. HEAL = Heal targeted units. Note: a
   * heal can't extend the initial power of the target(s). BOOST = Boost targeted units. Note: a
   * boost can extend the initial power of the target(s). SWAP = Change row (MELEE -> RANGED and
   * vice versa) of targeted units.
   */
  private enum ActionType {
    DAMAGE,
    HEAL,
    BOOST,
    SWAP;
  }

  private int targetingCardUUID;
  private ArrayList<Integer> targetedCardsUUIDs;
  private int points;
  private boolean hasRandomTargets;
  private boolean canTargetAlliedUnits;
  private boolean canTargetEnemyUnits;
  private ActionType actionType;

  /**
   * @param targetingCardUUID UUID of the card which is targeting.
   * @param targetedCardsUUIDs List of UUIDs of the targeted cards.
   * @param points Number of points which will be applied on the action on each targeted unit.
   * @param hasRandomTargets Determines if the targets are chosen at random or by the player.
   * @param canTargetAlliedUnits Determines if it is possible to target allied units.
   * @param canTargetEnemyUnits Determines if it is possible to target enemy units.
   * @param actionType Type of the Action (There are currently 4 types: DAMAGE, HEAL, BOOST, SWAP)
   */
  public TargetUnitAction(
      int targetingCardUUID,
      ArrayList<Integer> targetedCardsUUIDs,
      int points,
      boolean hasRandomTargets,
      boolean canTargetAlliedUnits,
      boolean canTargetEnemyUnits,
      ActionType actionType) {
    this.targetingCardUUID = targetingCardUUID;
    this.targetedCardsUUIDs = targetedCardsUUIDs;
    this.points = points;
    this.hasRandomTargets = hasRandomTargets;
    this.canTargetAlliedUnits = canTargetAlliedUnits;
    this.canTargetEnemyUnits = canTargetEnemyUnits;
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
    return hasRandomTargets;
  }

  public boolean canTargetAlliedUnits() {
    return canTargetAlliedUnits;
  }

  public boolean canTargetEnemyUnits() {
    return canTargetEnemyUnits;
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
