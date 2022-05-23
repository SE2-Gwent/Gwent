package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/*
this class is used for the boost ability
note: a boost can extend the initial power of the unit
 */
public class BoostParams extends ActionParams {
  private int boostingCardUUID;
  private ArrayList<Integer> boostedCardUUIDs;

  public BoostParams(int boostingCardUUID, ArrayList<Integer> boostedCardsUUID) {
    this.boostingCardUUID = boostingCardUUID;
    this.boostedCardUUIDs = boostedCardsUUID;
  }

  public int getBoostingCardUUID() {
    return boostingCardUUID;
  }

  public ArrayList<Integer> getBoostedCardUUIDs() {
    return boostedCardUUIDs;
  }

  public void setBoostingCardUUID(int boostingCardUUID) {
    this.boostingCardUUID = boostingCardUUID;
  }

  public void setBoostedCardUUIDs(ArrayList<Integer> boostedCardUUIDs) {
    this.boostedCardUUIDs = boostedCardUUIDs;
  }
}
