package at.aau.se2.gamelogic.models.heroactions;

import java.util.ArrayList;

public class AttackParams extends HeroActionParams {
  private final ArrayList<Integer> attackedCardUUIDs;

  public AttackParams(ArrayList<Integer> attackedCardUUIDs) {
    this.attackedCardUUIDs = attackedCardUUIDs;
  }

  public ArrayList<Integer> getAttackedCardUUIDs() {
    return attackedCardUUIDs;
  }
}
