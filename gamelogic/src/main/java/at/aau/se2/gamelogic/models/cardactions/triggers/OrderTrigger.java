package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.HashMap;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetRowAction;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetUnitAction;

/** This class is used to implement the order ability of a card. */
public class OrderTrigger {
  private HashMap<String, TargetRowAction> targetRowActions;
  private HashMap<String, TargetUnitAction> targetUnitActions;
  private int coolDown;
  private int remCoolDown;
  private boolean zeal;

  public OrderTrigger(
      HashMap<String, TargetRowAction> targetRowActions,
      HashMap<String, TargetUnitAction> targetUnitActions,
      int coolDown,
      boolean zeal) {
    this.targetRowActions = targetRowActions;
    this.targetUnitActions = targetUnitActions;
    this.coolDown = coolDown;
    this.remCoolDown = zeal ? 0 : coolDown;
    this.zeal = zeal;
  }

  @Keep
  public OrderTrigger() {}

  public HashMap<String, TargetRowAction> getTargetRowActions() {
    return targetRowActions;
  }

  public void setTargetRowActions(HashMap<String, TargetRowAction> targetRowActions) {
    this.targetRowActions = targetRowActions;
  }

  public HashMap<String, TargetUnitAction> getTargetUnitActions() {
    return targetUnitActions;
  }

  public void setTargetUnitActions(HashMap<String, TargetUnitAction> targetUnitActions) {
    this.targetUnitActions = targetUnitActions;
  }

  public int getCoolDown() {
    return coolDown;
  }

  public void setCoolDown(int coolDown) {
    this.coolDown = coolDown;
  }

  public int getRemCoolDown() {
    return remCoolDown;
  }

  public void setRemCoolDown(int remCoolDown) {
    this.remCoolDown = remCoolDown;
  }

  public boolean hasZeal() {
    return zeal;
  }

  public void setZeal(boolean zeal) {
    this.zeal = zeal;
  }
}
