package at.aau.se2.gwent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.CardActionCallback;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;

public class GameLogicTest {
  private final ArrayList<Card> testCards = new ArrayList<>();
  private CardDecks cardDecks;
  private GameLogic sut;

  CardActionCallback mockCallback;

  @Before
  public void setup() {
    testCards.add(new Card(1));
    testCards.add(new Card(2));
    cardDecks = new CardDecks(testCards, testCards);
    sut = new GameLogic(Player.INITIATOR, cardDecks);

    mockCallback = mock(CardActionCallback.class);
    sut.registerCardActionCallback(mockCallback);
  }

  @Test
  public void testPerformActionMappings() {
    HashMap<CardAction, ActionParams> testData = new HashMap<>();
    testData.put(new CardAction(CardAction.ActionType.DEPLOY), new DeployParams(1, Row.MELEE, 0));
    // add other actions here to test

    for (Map.Entry<CardAction, ActionParams> entry : testData.entrySet()) {
      sut.performAction(entry.getKey(), entry.getValue());

      assertTrue(entry.getKey().isPerformed());
      verify(mockCallback).didPerformAction(eq(entry.getKey()), eq(entry.getValue()));
    }
  }

  @Test
  public void testPerformActionWithWrongParams() {
    HashMap<CardAction, ActionParams> testData = new HashMap<>();
    testData.put(new CardAction(CardAction.ActionType.DEPLOY), new AttackParams());
    // add other actions here to test

    for (Map.Entry<CardAction, ActionParams> entry : testData.entrySet()) {
      sut.performAction(entry.getKey(), entry.getValue());

      assertFalse(entry.getKey().isPerformed());
      verifyNoInteractions(mockCallback);
    }
  }

  @Test
  public void testPerformCardActionsResults() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);
    CardAction action2 = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new DeployParams(1, Row.MELEE, 0));
    sut.performAction(action2, new DeployParams(2, Row.MELEE, 0));

    assertEquals(2, sut.getGameFieldRows().meleeRowFor(Player.INITIATOR).size());
    assertEquals(2, sut.getGameFieldRows().meleeRowFor(Player.INITIATOR).get(0).getId());
  }
}
