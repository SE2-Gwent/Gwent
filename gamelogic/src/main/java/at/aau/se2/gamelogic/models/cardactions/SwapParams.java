package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

/*
this class is used for the swap ability
which means the swappingCard targets up to n cards (swappedCardUUIds)
each target is moved to the other row (MELEE -> RANGED and vice versa) if possible
 */
public class SwapParams extends ActionParams {
  private int swappingCardUUID;
  private ArrayList<Integer> swappedCardUUIds;

  public SwapParams(int swappingCardUUID, ArrayList<Integer> swappedCardUUIds) {
    this.swappingCardUUID = swappingCardUUID;
    this.swappedCardUUIds = swappedCardUUIds;
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
}
