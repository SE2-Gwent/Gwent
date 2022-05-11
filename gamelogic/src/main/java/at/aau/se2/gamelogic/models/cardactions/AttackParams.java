package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

public class AttackParams extends ActionParams {
  private final int attackingCardUUID;
  private final ArrayList<Integer> attackedCardUUIDs;

  public AttackParams(int attackingCardUUID, ArrayList<Integer> attackedCardUUIDs) {
    this.attackingCardUUID = attackingCardUUID;
    this.attackedCardUUIDs = attackedCardUUIDs;
  }

  public int getAttackingCardUUID() {
    return attackingCardUUID;
  }

  public ArrayList<Integer> getAttackedCardUUIDs() {
    return attackedCardUUIDs;
  }
}
