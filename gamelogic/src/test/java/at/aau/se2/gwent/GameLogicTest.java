package at.aau.se2.gwent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;

public class GameLogicTest {
  ArrayList<Card> testCards = new ArrayList<>();
  CardDecks cardDecks;
  GameLogic sut;

  @Before
  public void setup() {
    testCards.add(new Card(1));
    testCards.add(new Card(2));
    cardDecks = new CardDecks(testCards, testCards);
    sut = new GameLogic(cardDecks);
  }

  @Test
  public void testPerformActionMappings() {
    HashMap<CardAction, ActionParams> testData = new HashMap<>();
    testData.put(new CardAction(CardAction.ActionType.DEPLOY), new DeployParams(1, Row.MELEE, 0));

    for (Map.Entry<CardAction, ActionParams> entry : testData.entrySet()) {
      sut.performAction(entry.getKey(), entry.getValue());
      assertTrue(entry.getKey().isPerformed());
    }
  }

  @Test
  public void testPerformActionWithWrongParams() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new AttackParams());

    assertFalse(action.isPerformed());
  }

  @Test
  public void testPerformCardActionsResults() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);
    CardAction action2 = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new DeployParams(1, Row.MELEE, 0));
    sut.performAction(action2, new DeployParams(2, Row.MELEE, 0));

    assertEquals(2, sut.getGameFieldRows().meleeRowFor(Player.ME).size());
    assertEquals(2, sut.getGameFieldRows().meleeRowFor(Player.ME).get(0).getId());
  }
}
