package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gamelogic.models.Hero;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.FogParams;
import at.aau.se2.gamelogic.util.Log;

public class GameLogic {
  private GameField gameField;
  private ArrayList<CardActionCallback> cardActionCallbacks = new ArrayList<>();

  public GameLogic(Player currentPlayer, CardDecks cardDecks) {
    GameFieldRows gameFieldRows = new GameFieldRows();
    ArrayList<Hero> heroes = new ArrayList<>();
    // TODO: Initialize Gamefield with data(Cards, heroes, ..)
    gameField =
        new GameField(
            gameFieldRows, currentPlayer, new Player(2, InitialPlayer.OPPONENT), cardDecks, heroes);
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

        Card card =
            gameField
                .getCardDecks()
                .getCard(deployParams.getCardUUID(), gameField.getCurrentPlayer());
        deployCard(card, deployParams.getRow(), deployParams.getPosition());

        // TODO: loop over list which contains properties and call performAction again for each
        // elem.
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

      case HEAL:
        // TODO: implement heal unit(s)
        break;

      case BOOST:
        // TODO: implement boost unit(s)
        break;

      case SWAP:
        // TODO: implement swap units(s)
        break;

      case ORDER:
        // TODO: implement order ability

        // TODO: if coolDowmRem == 0 -> loop over list which contains properties and call
        // performAction again for each elem.
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
        cardRow = gameField.getRows().meleeRowFor(gameField.getCurrentPlayer());
        break;
      case RANGED:
        cardRow = gameField.getRows().rangedRowFor(gameField.getCurrentPlayer());
        break;
    }
    cardRow.add(position, card);
  }

  private Player getOpponent() {
    if (gameField.getCurrentPlayer().getInitialPlayerInformation() == InitialPlayer.INITIATOR) {
      return gameField.getOpponent();
    }
    return gameField.getCurrentPlayer();
  }

  private void notifyCardActionCallbacks(CardAction action, ActionParams params) {
    for (CardActionCallback callback : cardActionCallbacks) {
      callback.didPerformAction(action, params);
    }
  }

  public GameFieldRows getGameFieldRows() {
    return gameField.getRows();
  }

  public void registerCardActionCallback(CardActionCallback callback) {
    if (cardActionCallbacks.contains(callback)) {
      return;
    }

    cardActionCallbacks.add(callback);
  }
}
