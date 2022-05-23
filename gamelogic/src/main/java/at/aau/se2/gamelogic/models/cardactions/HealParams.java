package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/*
this class is used for healing effects
note: a unit can't receive more power than it's initial power (if healPoints > powerDiff)
 */
public class HealParams extends ActionParams {
  private int healingCardUUID;
  private ArrayList<Integer> healedCardUUIDs;
  private int healPoints;

  public HealParams(int boostingCardUUID, ArrayList<Integer> boostedCardUUIDs, int healPoints) {
    this.healingCardUUID = boostingCardUUID;
    this.healedCardUUIDs = boostedCardUUIDs;
    this.healPoints = healPoints;
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
}
