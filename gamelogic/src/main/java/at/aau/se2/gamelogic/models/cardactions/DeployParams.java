package at.aau.se2.gamelogic.models.cardactions;

import java.util.HashMap;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.models.Row;

/**
 * This class is used for deploying cards on rows and to store all abilities related the the DEPLOY
 * keyword (Triggered when deployed)
 */
public class DeployParams extends ActionParams {
  private int cardUUID;
  private Row row;
  private int position;
  HashMap<CardAction, ActionParams> triggeredActions;

  /**
   * @param cardUUID UUID of the card to deploy.
   * @param row Row where the card should get deployed.
   * @param position Deploy position within the row.
   * @param triggeredActions A Hashmap contains all actions which are triggered on deploy.
   */
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
