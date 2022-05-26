package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.cardactions.actions.CardAction;

/** This class is used to implement the order ability of a card. */
public class OrderTrigger {
  private final ArrayList<CardAction> cardActions;
  private final int coolDown;
  private int remCoolDown;
  private final boolean hasZeal;

  /**
   * @param cardActions List of actions which will be executed if the order ability is triggered.
   * @param coolDown CoolDown of the Order ability (after activating the order ability remCoolDown
   *     is set to coolDown.
   * @param hasZeal If this value is set to true, remCoolDown is set to zero at initialization.
   */
  public OrderTrigger(ArrayList<CardAction> cardActions, int coolDown, boolean hasZeal) {
    this.cardActions = cardActions;
    this.coolDown = coolDown;
    this.hasZeal = hasZeal;
    if (hasZeal) {
      this.remCoolDown = 0;
    } else {
      this.remCoolDown = coolDown;
    }
  }

  public ArrayList<CardAction> getCardActions() {
    return cardActions;
  }

  public int getCoolDown() {
    return coolDown;
  }

  public int getRemCoolDown() {
    return remCoolDown;
  }

  public void setRemCoolDown(int remCoolDown) {
    this.remCoolDown = remCoolDown;
  }
}
