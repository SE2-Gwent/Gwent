package at.aau.se2.gamelogic.models.cardactions.triggers;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.cardactions.actions.CardAction;

/** This class is used to implement the deploy ability of a card. */
public class DeployTrigger {
  private ArrayList<CardAction> cardActions;

  /**
   * @param cardActions a list of cardActions which will be executed after the card gets deployed.
   */
  public DeployTrigger(ArrayList<CardAction> cardActions) {
    this.cardActions = cardActions;
  }

  public ArrayList<CardAction> getCardActions() {
    return cardActions;
  }
}
