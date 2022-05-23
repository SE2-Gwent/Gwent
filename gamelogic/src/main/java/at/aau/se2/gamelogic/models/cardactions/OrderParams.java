package at.aau.se2.gamelogic.models.cardactions;

import java.util.HashMap;

import at.aau.se2.gamelogic.CardAction;

/*
this class is used for order abilities
it stores the initialCoolDown and the remaining CoolDown (number of allied turn until the ability
can be activated again)
it contains a Hashmap which contains all abilities which will be executed, if the order ability
of the card is activated
 */
public class OrderParams extends ActionParams {
  private final int coolDown;
  private int coolDownRem;
  HashMap<CardAction, ActionParams> triggeredActions = new HashMap<>();

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
