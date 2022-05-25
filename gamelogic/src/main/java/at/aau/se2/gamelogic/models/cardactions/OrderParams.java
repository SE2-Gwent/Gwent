package at.aau.se2.gamelogic.models.cardactions;

import java.util.HashMap;

import at.aau.se2.gamelogic.CardAction;

/** This class is used to implement order abilities. */
public class OrderParams extends ActionParams {
  private final int coolDown;
  private int coolDownRem;
  HashMap<CardAction, ActionParams> triggeredActions = new HashMap<>();

  /**
   * @param coolDown Number of allied turns to wait (static value)
   * @param coolDownRem Remaining number of allied turns until ability can be activated again (if
   *     the card has ZEAL, this is initialy set to 0)
   * @param triggeredActions Hashmap which contains actions which are executed if the ability is
   *     activated.
   */
  public OrderParams(
      int coolDown, int coolDownRem, HashMap<CardAction, ActionParams> triggeredActions) {
    this.coolDown = coolDown;
    this.coolDownRem = coolDownRem;
    this.triggeredActions = triggeredActions;
  }

  public int getCoolDown() {
    return coolDown;
  }

  public int getCoolDownRem() {
    return coolDownRem;
  }

  public HashMap<CardAction, ActionParams> getTriggeredActions() {
    return triggeredActions;
  }

  public void setCoolDownRem(int coolDownRem) {
    this.coolDownRem = coolDownRem;
  }

  public void setTriggeredActions(HashMap<CardAction, ActionParams> triggeredActions) {
    this.triggeredActions = triggeredActions;
  }
}
