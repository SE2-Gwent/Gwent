package at.aau.se2.gamelogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import at.aau.se2.gamelogic.comunication.FirebaseConnector;
import at.aau.se2.gamelogic.comunication.Result;
import at.aau.se2.gamelogic.comunication.ResultObserver;
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
  private FirebaseConnector mockConnector;
  private GameStateMachine mockGameStateMachine;

  CardActionCallback mockCallback;

  @Before
  public void setup() {
    testCards.add(
        new Card(1, "TestCard 1", 1, 0, "This is a test Card", new ArrayList<ActionParams>()));
    testCards.add(
        new Card(
            2, "TestCard 2", 3, 0, "This is another test Card", new ArrayList<ActionParams>()));
    cardDecks = new CardDecks(testCards, testCards);
    currentPlayer = new Player(1, InitialPlayer.INITIATOR);
    meleeRow = new Row(1, RowType.MELEE);

    mockConnector = mock(FirebaseConnector.class);
    mockGameStateMachine = mock(GameStateMachine.class);
    sut = new GameLogic(mockConnector, mockGameStateMachine);
    mockCallback = mock(CardActionCallback.class);
    sut.registerCardActionCallback(mockCallback);
  }

  @Test
  public void testPerformActionMappings() {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.stateEquals(any())).thenReturn(true);
    sut.initializeGame(currentPlayer, cardDecks, new ArrayList<>());

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
      verify(mockCallback)
          .didPerformAction(
              ArgumentMatchers.eq(entry.getKey()), ArgumentMatchers.eq(entry.getValue()));
    }
  }

  @Test
  public void testPerformActionWithWrongParams() {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    sut.initializeGame(currentPlayer, cardDecks, new ArrayList<>());

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
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.stateEquals(any())).thenReturn(true);
    sut.initializeGame(currentPlayer, cardDecks, new ArrayList<>());

    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);
    CardAction action2 = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new DeployParams(1, meleeRow, 0));
    sut.performAction(action2, new DeployParams(2, meleeRow, 0));

    assertEquals(2, sut.getGameFieldRows().meleeRowFor(currentPlayer).size());
    assertEquals(2, sut.getGameFieldRows().meleeRowFor(currentPlayer).get(0).getId());
  }

  @Test
  public void testStartGameFails() {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.startGame()).thenReturn(false);

    // Capture the ResultObserver to call it later via Mockito
    ArgumentCaptor<ResultObserver<Integer, Error>> observerCapture =
        ArgumentCaptor.forClass(ResultObserver.class);

    sut.startGame(
        new ResultObserver<Integer, Error>() {
          @Override
          public void finished(Result<Integer, Error> result) {
            assertFalse(result.isSuccessful());
            assertEquals(-1, sut.getGameId());
          }
        });

    // Save resultObserver
    verify(mockConnector).createGame(observerCapture.capture());
    // Call resultObserver
    observerCapture.getValue().finished(Result.Failure(new Error()));
    // Wait for gameLogic.startGame
    verify(mockConnector, timeout(1000).atLeastOnce()).createGame(any());
  }

  @Test
  public void testStartGame() throws InterruptedException {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.startGame()).thenReturn(true);

    ArgumentCaptor<ResultObserver<Integer, Error>> observerCapture =
        ArgumentCaptor.forClass(ResultObserver.class);
    sut.startGame(
        new ResultObserver<Integer, Error>() {
          @Override
          public void finished(Result<Integer, Error> result) {
            assertTrue(result.isSuccessful());
            assertEquals(1, sut.getGameId());
          }
        });

    verify(mockConnector).createGame(observerCapture.capture());
    observerCapture.getValue().finished(Result.Success(1));
    verify(mockConnector, timeout(1000).atLeastOnce()).createGame(any());
  }

  @Test
  public void testStartGameAlreadyExecuted() throws InterruptedException {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(false);

    CountDownLatch latch = new CountDownLatch(1);

    sut.startGame(
        new ResultObserver<Integer, Error>() {
          @Override
          public void finished(Result<Integer, Error> result) {
            assertFalse(result.isSuccessful());
            latch.countDown();
          }
        });

    latch.await();
  }
}
