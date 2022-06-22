package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.HashMap;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetRowAction;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetUnitAction;

/** This class is used to implement the deploy ability of a card. */
public class DeployTrigger {
  private HashMap<String, TargetRowAction> targetRowActions;
  private HashMap<String, TargetUnitAction> targetUnitActions;

  public DeployTrigger(
      HashMap<String, TargetRowAction> targetRowActions,
      HashMap<String, TargetUnitAction> targetUnitActions) {
    this.targetRowActions = targetRowActions;
    this.targetUnitActions = targetUnitActions;
  }

  @Keep
  public DeployTrigger() {}

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
}
