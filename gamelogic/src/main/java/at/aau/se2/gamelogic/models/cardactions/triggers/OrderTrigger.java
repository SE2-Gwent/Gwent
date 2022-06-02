package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.cardactions.actions.TargetRowAction;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetUnitAction;

/** This class is used to implement the order ability of a card. */
public class OrderTrigger {
  private ArrayList<TargetRowAction> targetRowActions;
  private ArrayList<TargetUnitAction> targetUnitActions;
  private int coolDown;
  private int remCoolDown;
  private boolean zeal;

  public OrderTrigger(
      ArrayList<TargetRowAction> targetRowActions,
      ArrayList<TargetUnitAction> targetUnitActions,
      int coolDown,
      boolean zeal) {
    this.targetRowActions = targetRowActions;
    this.targetUnitActions = targetUnitActions;
    this.coolDown = coolDown;
    this.remCoolDown = zeal ? 0 : coolDown;
    this.zeal = zeal;
  }

  public ArrayList<TargetRowAction> getTargetRowActions() {
    return targetRowActions;
  }

  public void setTargetRowActions(ArrayList<TargetRowAction> targetRowActions) {
    this.targetRowActions = targetRowActions;
  }

  public ArrayList<TargetUnitAction> getTargetUnitActions() {
    return targetUnitActions;
  }

  public void setTargetUnitActions(ArrayList<TargetUnitAction> targetUnitActions) {
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
