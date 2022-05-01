package at.aau.se2.gamelogic.models.heroactions;

import java.util.ArrayList;

public class HeroDoDamageParams extends HeroActionParams {
  private final ArrayList<Integer> attackedCardUUIDs;

  public HeroDoDamageParams(ArrayList<Integer> attackedCardUUIDs) {
    this.attackedCardUUIDs = attackedCardUUIDs;
  }

  public ArrayList<Integer> getAttackedCardUUIDs() {
    return attackedCardUUIDs;
  }
}
