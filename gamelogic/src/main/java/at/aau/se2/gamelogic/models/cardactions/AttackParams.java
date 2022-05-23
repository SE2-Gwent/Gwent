package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/*
this class is used for damage effects (i would suggest to rename it later to DamageParams)
 */
public class AttackParams extends ActionParams {
  private int attackingCardUUID;
  private ArrayList<Integer> attackedCardUUIDs;
  private int damagePoints;

  public AttackParams(
      int attackingCardUUID, ArrayList<Integer> attackedCardUUIDs, int damagePoints) {
    this.attackingCardUUID = attackingCardUUID;
    this.attackedCardUUIDs = attackedCardUUIDs;
    this.damagePoints = damagePoints;
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
}
