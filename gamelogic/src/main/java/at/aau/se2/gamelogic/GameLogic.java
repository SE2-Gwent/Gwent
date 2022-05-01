package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.FogParams;
import at.aau.se2.gamelogic.util.Log;

public class GameLogic {
  private Player me;
  private Player opponent;
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
        break;

      case ATTACK:
        AttackParams attackParams = (params instanceof AttackParams ? (AttackParams) params : null);
        if (attackParams == null) return;
        // TODO: implement card attacking
        break;

      case FOG:
        FogParams fogParams = (params instanceof FogParams ? (FogParams) params : null);
        if (fogParams == null) return;
        // TODO: implement row fogging
        break;

      default:
        Log.w("Action not in performAction implemented");
    }

    action.performed = true;
    notifyCardActionCallbacks(action, params);
  }

  private void deployCard(Card card, Row row, int position) {
    ArrayList<Card> cardRow = null;

    switch (row.getRowType()) {
      case MELEE:
        cardRow = gameFieldRows.meleeRowFor(me);
        break;
      case RANGED:
        cardRow = gameFieldRows.rangedRowFor(me);
        break;
    }
    cardRow.add(position, card);
  }

  private Player getOpponent() {
    if (me.getInitialPlayerInformation() == InitialPlayer.INITIATOR) {
      return opponent;
    }
    return me;
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
