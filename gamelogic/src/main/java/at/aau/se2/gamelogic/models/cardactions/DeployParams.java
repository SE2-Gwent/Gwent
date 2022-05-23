package at.aau.se2.gamelogic.models.cardactions;

import java.util.HashMap;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.models.Row;

/*
this class is used for deploying a card on a row and to trigger abilities which will be
executed on deploy. See Hashmap (works in the same way as the order ability)
 */
public class DeployParams extends ActionParams {
  private int cardUUID;
  private Row row;
  private int position;
  HashMap<CardAction, ActionParams> triggeredActions;

  public DeployParams(
      int cardUUID, Row row, int position, HashMap<CardAction, ActionParams> triggeredActions) {
    this.cardUUID = cardUUID;
    this.row = row;
    this.position = position;
    this.triggeredActions = triggeredActions;
  }

  public int getCardUUID() {
    return cardUUID;
  }

  public Row getRow() {
    return row;
  }

  public int getPosition() {
    return position;
  }

  public HashMap<CardAction, ActionParams> getTriggeredActions() {
    return triggeredActions;
  }

  public void setCardUUID(int cardUUID) {
    this.cardUUID = cardUUID;
  }

  public void setRow(Row row) {
    this.row = row;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public void setTriggeredActions(HashMap<CardAction, ActionParams> triggeredActions) {
    this.triggeredActions = triggeredActions;
  }
}
