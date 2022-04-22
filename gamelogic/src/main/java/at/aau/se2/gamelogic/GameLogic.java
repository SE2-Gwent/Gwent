package at.aau.se2.gamelogic;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.GameFieldRows;

public class GameLogic {

  private Player whoami = Player.ME;
  private GameFieldRows gameFieldRows = new GameFieldRows();
  private CardDecks cardDecks;

  public GameLogic(CardDecks cardDecks) {
    this.cardDecks = cardDecks;
  }

  public void performAction(CardAction action, ActionParams params) {
    if (action.performed) return;

    switch (action.getType()) {
      case DEPLOY:
        DeployParams deployParams = (params instanceof DeployParams ? (DeployParams) params : null);
        if (deployParams == null) return;
        Card card = cardDecks.getCard(deployParams.getCardUUID(), whoami);
        deployCard(card, deployParams.getRow(), deployParams.getPosition());
    }

    action.performed = true;
  }

  private void deployCard(Card card, Row row, int position) {
    ArrayList<Card> cardRow = null;

    switch (row) {
      case MELEE:
        cardRow = gameFieldRows.meleeRowFor(whoami);
        break;
      case RANGED:
        cardRow = gameFieldRows.rangedRowFor(whoami);
        break;
    }
    cardRow.add(position, card);
  }

  public GameFieldRows getGameFieldRows() {
    return gameFieldRows;
  }
}
