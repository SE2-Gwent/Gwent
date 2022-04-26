package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.GameFieldRows;
import at.aau.se2.gamelogic.util.Log;

public class GameLogic {

  private Player me;
  private CardDecks cardDecks;
  private GameFieldRows gameFieldRows = new GameFieldRows();
  private ArrayList<CardActionCallback> cardActionCallbacks = new ArrayList<>();

  public GameLogic(Player player, CardDecks cardDecks) {
    this.me = player;
    this.cardDecks = cardDecks;
  }

  public void performAction(CardAction action, ActionParams params) {
    if (action.performed) {
      Log.w("Action is already performed.");
      return;
    }

    switch (action.getType()) {
      case DEPLOY:
        DeployParams deployParams = (params instanceof DeployParams ? (DeployParams) params : null);
        if (deployParams == null) return;

        Card card = cardDecks.getCard(deployParams.getCardUUID(), me);
        deployCard(card, deployParams.getRow(), deployParams.getPosition());
    }

    action.performed = true;
    notifyCardActionCallbacks(action, params);
  }

  private void deployCard(Card card, Row row, int position) {
    ArrayList<Card> cardRow = null;

    switch (row) {
      case MELEE:
        cardRow = gameFieldRows.meleeRowFor(me);
        break;
      case RANGED:
        cardRow = gameFieldRows.rangedRowFor(me);
        break;
    }
    cardRow.add(position, card);
  }

  private void notifyCardActionCallbacks(CardAction action, ActionParams params) {
    for (CardActionCallback callback : cardActionCallbacks) {
      callback.didPerformAction(action, params);
    }
  }

  public GameFieldRows getGameFieldRows() {
    return gameFieldRows;
  }

  public void registerCardActionCallback(CardActionCallback callback) {
    if (cardActionCallbacks.contains(callback)) {
      return;
    }

    cardActionCallbacks.add(callback);
  }
}
