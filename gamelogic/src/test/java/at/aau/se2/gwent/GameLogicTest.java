package at.aau.se2.gwent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.CardActionCallback;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.FogParams;

public class GameLogicTest {
  private final ArrayList<Card> testCards = new ArrayList<>();
  private CardDecks cardDecks;
  private GameLogic sut;
  private Player currentPlayer;
  private Row meleeRow;

  CardActionCallback mockCallback;

  @Before
  public void setup() {
    testCards.add(new Card(1, "TestCard 1", 1, 0, "This is a test Card"));
    testCards.add(new Card(2, "TestCard 2", 3, 0, "This is another test Card"));
    cardDecks = new CardDecks(testCards, testCards);
    currentPlayer = new Player(1, InitialPlayer.INITIATOR);
    meleeRow = new Row(1, RowType.MELEE);
    sut = new GameLogic(currentPlayer, cardDecks);
    mockCallback = mock(CardActionCallback.class);
    sut.registerCardActionCallback(mockCallback);
  }

  @Test
  public void testPerformActionMappings() {
    HashMap<CardAction, ActionParams> testData = new HashMap<>();
    testData.put(new CardAction(CardAction.ActionType.DEPLOY), new DeployParams(1, meleeRow, 0));
    testData.put(
        new CardAction(CardAction.ActionType.ATTACK),
        new AttackParams(0, new ArrayList<Integer>(Arrays.asList(1, 2))));
    testData.put(new CardAction(CardAction.ActionType.FOG), new FogParams(meleeRow));
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
    testData.put(
        new CardAction(CardAction.ActionType.DEPLOY),
        new AttackParams(0, new ArrayList<>(Arrays.asList(1, 2))));
    testData.put(new CardAction(CardAction.ActionType.ATTACK), new FogParams(meleeRow));
    testData.put(
        new CardAction(CardAction.ActionType.FOG),
        new AttackParams(0, new ArrayList<>(Arrays.asList(1, 2))));
    // add other actions here to test

    for (Map.Entry<CardAction, ActionParams> entry : testData.entrySet()) {
      sut.performAction(entry.getKey(), entry.getValue());

      assertFalse(entry.getKey().isPerformed());
      verifyNoInteractions(mockCallback);
    }
  }

  @Test
  public void testPerformDeployCardActionResults() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);
    CardAction action2 = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new DeployParams(1, meleeRow, 0));
    sut.performAction(action2, new DeployParams(2, meleeRow, 0));

    assertEquals(2, sut.getGameFieldRows().meleeRowFor(currentPlayer).size());
    assertEquals(2, sut.getGameFieldRows().meleeRowFor(currentPlayer).get(0).getId());
  }
}
