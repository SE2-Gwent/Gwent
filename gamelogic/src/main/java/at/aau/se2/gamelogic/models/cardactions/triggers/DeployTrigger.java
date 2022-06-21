package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.ArrayList;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetRowAction;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetUnitAction;

/** This class is used to implement the deploy ability of a card. */
public class DeployTrigger {
  private ArrayList<TargetRowAction> targetRowActions;
  private ArrayList<TargetUnitAction> targetUnitActions;

  public DeployTrigger(
      ArrayList<TargetRowAction> targetRowActions, ArrayList<TargetUnitAction> targetUnitActions) {
    this.targetRowActions = targetRowActions;
    this.targetUnitActions = targetUnitActions;
  }

  @Keep
  public DeployTrigger() {}

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
}
