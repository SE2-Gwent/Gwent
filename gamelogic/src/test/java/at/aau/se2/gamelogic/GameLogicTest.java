package at.aau.se2.gamelogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import at.aau.se2.gamelogic.comunication.FirebaseConnector;
import at.aau.se2.gamelogic.comunication.Result;
import at.aau.se2.gamelogic.comunication.ResultObserver;
import at.aau.se2.gamelogic.comunication.SyncAction;
import at.aau.se2.gamelogic.comunication.SyncRoot;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.CardType;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gamelogic.models.cardactions.triggers.DeployTrigger;
import at.aau.se2.gamelogic.models.cardactions.triggers.OrderTrigger;
import at.aau.se2.gamelogic.state.GameState;

public class GameLogicTest {
  private final ArrayList<Card> testCards = new ArrayList<>();
  private CardDecks cardDecks;
  private GameLogic sut;
  private Player currentPlayer;
  private Row meleeRow;
  private FirebaseConnector mockConnector;
  private GameStateMachine mockGameStateMachine;

  @Before
  public void setup() {
    setupTestCards();
    cardDecks = new CardDecks(testCards, testCards);
    currentPlayer = new Player(1, InitialPlayer.INITIATOR);
    meleeRow = new Row(1, RowType.MELEE);

    mockConnector = mock(FirebaseConnector.class);
    mockGameStateMachine = mock(GameStateMachine.class);
    sut = new GameLogic(mockConnector, mockGameStateMachine);
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
  public void testStartGame() {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.stateEquals(any())).thenReturn(true);
    when(mockGameStateMachine.startGame()).thenReturn(true);

    ArgumentCaptor<ResultObserver<Integer, Error>> observerCapture =
        ArgumentCaptor.forClass(ResultObserver.class);
    sut.startGame(
        new ResultObserver<Integer, Error>() {
          @Override
          public void finished(Result<Integer, Error> result) {
            assertTrue(result.isSuccessful());
            assertEquals(1, sut.getGameId());
            assertEquals(InitialPlayer.INITIATOR, sut.getWhoAmI());
            assertNotNull(sut.getGameField());
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

  @Test
  public void testJoinGame() {
    when(mockGameStateMachine.canProgressTo(any())).thenReturn(true);
    when(mockGameStateMachine.joinGame()).thenReturn(true);

    ArgumentCaptor<ResultObserver<GameField, Error>> observerCapture =
        ArgumentCaptor.forClass(ResultObserver.class);

    sut.joinGame(
        1,
        new ResultObserver<GameField, Error>() {
          @Override
          public void finished(Result<GameField, Error> result) {
            assertTrue(result.isSuccessful());
            assertEquals(InitialPlayer.OPPONENT, sut.getWhoAmI());
          }
        });

    verify(mockConnector).joinGame(eq(1), observerCapture.capture());
    observerCapture.getValue().finished(Result.Success(new GameField()));
    verify(mockConnector, timeout(1000).atLeastOnce()).joinGame(eq(1), any());
  }

  @Test
  public void testHandleGameSyncUpdatesOpponentJoined() {
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.WAIT_FOR_OPPONENT);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).opponentJoined();
    ArgumentCaptor<SyncAction> captor = ArgumentCaptor.forClass(SyncAction.class);
    verify(mockConnector, times(1)).sendSyncAction(captor.capture());
    assertEquals(SyncAction.Type.STARTING_PLAYER, captor.getValue().getType());
  }

  @Test
  public void testHandleGameSyncUpdatesStartingPlayer() {
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    SyncAction startingSyncAction =
        new SyncAction(SyncAction.Type.STARTING_PLAYER, InitialPlayer.OPPONENT.name());

    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    gameField.setCardDeckFor(InitialPlayer.INITIATOR, testCards);
    gameField.setCardDeckFor(InitialPlayer.OPPONENT, testCards);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockSyncRoot.getLastActions(eq(0)))
        .thenReturn(new ArrayList(Collections.singletonList(startingSyncAction)));
    when(mockGameStateMachine.getCurrent())
        .thenReturn(GameState.START_GAME_ROUND, GameState.WAIT_FOR_OPPONENT);
    when(mockGameStateMachine.roundCanStart()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    assertEquals(InitialPlayer.OPPONENT, sut.getStartingPlayer());
    assertEquals(3, sut.getCardMulligansLeft());
  }

  @Test
  public void testHandleGameSyncStartGameCardDeckSetupForInnitiator() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    GameLogicDataProvider mockDataProvider = mock(GameLogicDataProvider.class);
    sut.setGameLogicDataProvider(mockDataProvider);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.START_GAME_ROUND);
    when(mockGameStateMachine.cardsDrawn()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockDataProvider).needsCardDeck();
  }

  @Test
  public void testHandleGameSyncStartGameCardDeckSetupForOpponent() {
    sut.setWhoAmI(InitialPlayer.OPPONENT);
    GameLogicDataProvider mockDataProvider = mock(GameLogicDataProvider.class);
    sut.setGameLogicDataProvider(mockDataProvider);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setCardDeckFor(InitialPlayer.INITIATOR, testCards);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.START_GAME_ROUND);
    when(mockGameStateMachine.cardsDrawn()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockDataProvider).needsCardDeck();
  }

  @Test
  public void testHandleGameSyncUpdatesDrawCards() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.DRAW_CARDS);
    when(mockGameStateMachine.cardsDrawn()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).cardsDrawn();
    ArgumentCaptor<GameField> captor = ArgumentCaptor.forClass(GameField.class);
    verify(mockConnector, times(1)).syncGameField(captor.capture());
    assertNotNull(captor.getValue().getCurrentHandCards());
    assertEquals(10, captor.getValue().getCurrentHandCards().getP1Deck().size());
  }

  @Test
  public void testMulliganCards() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);
    sut.setCardMulligansLeft(3);

    sut.mulliganCard(initialPlayerCards.get(0).getId());

    verify(mockConnector).syncGameField(any());
    assertEquals(2, sut.getCardMulligansLeft());
  }

  @Test
  public void testMulliganCardsMultipleTimes() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);
    sut.setCardMulligansLeft(3);

    sut.mulliganCard(initialPlayerCards.get(0).getId());
    sut.mulliganCard(initialPlayerCards.get(0).getId());
    sut.mulliganCard(initialPlayerCards.get(0).getId());
    sut.mulliganCard(initialPlayerCards.get(0).getId());

    verify(mockConnector, times(3)).syncGameField(any());
    assertEquals(0, sut.getCardMulligansLeft());
    ArgumentCaptor<SyncAction> captor = ArgumentCaptor.forClass(SyncAction.class);
    verify(mockConnector, times(1)).sendSyncAction(captor.capture());
    assertEquals(SyncAction.Type.MULLIGAN_COMPLETE, captor.getValue().getType());
  }

  @Test
  public void testHandleGameSyncUpdatesMulliganCards() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.MULLIGAN_CARDS);
    when(mockGameStateMachine.cardsChanged()).thenReturn(true);

    ArrayList<SyncAction> lastSyncActions =
        new ArrayList<>(
            Arrays.asList(
                new SyncAction(SyncAction.Type.MULLIGAN_COMPLETE, InitialPlayer.INITIATOR.name())));
    when(mockSyncRoot.getLastActions(eq(0))).thenReturn(lastSyncActions);
    HashMap<String, SyncAction> syncActions = new HashMap<>();
    syncActions.put("0_key", lastSyncActions.get(0));
    when(mockSyncRoot.getSyncActions()).thenReturn(syncActions);
    sut.handleGameSyncUpdates(mockSyncRoot);

    lastSyncActions =
        new ArrayList<>(
            Arrays.asList(
                new SyncAction(SyncAction.Type.MULLIGAN_COMPLETE, InitialPlayer.OPPONENT.name())));
    when(mockSyncRoot.getLastActions(eq(1))).thenReturn(lastSyncActions);
    sut.handleGameSyncUpdates(mockSyncRoot);

    assertTrue(20 != sut.getGameField().getCardDeck(InitialPlayer.INITIATOR).size());
    verify(mockGameStateMachine).cardsChanged();
  }

  @Test
  public void testHandleGameSyncUpdatesPlayerEndedTurn() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCardDecks(cardDecks);
    currentPlayer.setHasLastPlayed(true);
    sut.setStartingPlayer(currentPlayer.getInitialPlayerInformation());
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent())
        .thenReturn(GameState.START_PLAYER_TURN, GameState.END_PLAYER_TURN);
    when(mockGameStateMachine.endPlayerTurns()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).endPlayerTurns();
  }

  @Test
  public void testHandleGameSyncUpdatesRestartTurn() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCardDecks(cardDecks);
    sut.setGameField(gameField);
    currentPlayer.setHasLastPlayed(true);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, testCards);
    gameField.setPlayingCardsFor(InitialPlayer.OPPONENT, testCards);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.END_PLAYER_TURN);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).restartTurns();
    assertFalse(currentPlayer.isHasLastPlayed());
  }

  @Test
  public void testPassGame() {
    GameField gameField = new GameField();
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    sut.setStartingPlayer(InitialPlayer.INITIATOR);
    sut.setCurrentPlayerCanPass(true);
    gameField.setOpponent(currentPlayer);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);

    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    gameField.setCurrentPlayer(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.stateEquals(GameState.START_PLAYER_TURN)).thenReturn(true);

    sut.pass();

    verify(mockConnector, times(1)).syncGameField(any());
    ArgumentCaptor<GameField> captor = ArgumentCaptor.forClass(GameField.class);
    verify(mockConnector, times(1)).syncGameField(captor.capture());
    assertTrue(captor.getValue().getPlayer(InitialPlayer.INITIATOR).isHasPassed());
  }

  @Test
  public void testHandleGameSyncEndRoundWhenPassed() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCardDecks(cardDecks);
    currentPlayer.setHasPassed(true);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent())
        .thenReturn(GameState.END_PLAYER_TURN, GameState.END_ROUND);
    when(mockGameStateMachine.endRound()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).endRound();
    assertFalse(currentPlayer.isHasLastPlayed());
  }

  @Test
  public void testHandleGameSyncEndRoundWhenCardsRunOut() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent())
        .thenReturn(GameState.END_PLAYER_TURN, GameState.END_ROUND);
    when(mockGameStateMachine.endRound()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).endRound();
    assertFalse(currentPlayer.isHasLastPlayed());
  }

  @Test
  public void testEndRoundRoundCount() {
    Player otherPlayer = new Player(2, InitialPlayer.OPPONENT);

    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(otherPlayer);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, testCards);
    gameField.setPlayingCardsFor(InitialPlayer.OPPONENT, testCards);
    gameField.getRows().getMeleeRowForP1().put("0_index", testCards.get(0));
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.END_ROUND);
    when(mockGameStateMachine.restartRound()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockConnector).syncGameField(any());
    assertEquals(1, gameField.getPlayer(InitialPlayer.INITIATOR).getCurrentMatchPoints());
    assertEquals(1, sut.getGameField().getRoundNumber());
  }

  @Test
  public void testHandleGameSyncEndGame() {
    Player otherPlayer = new Player(2, InitialPlayer.OPPONENT);

    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    sut.setGameField(gameField);
    gameField.setCardDecks(cardDecks);
    currentPlayer.setHasPassed(true);
    currentPlayer.setCurrentMatchPoints(1);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(otherPlayer);
    gameField.getRows().getMeleeRowForP1().put("0_index", testCards.get(0));
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.END_ROUND);
    when(mockGameStateMachine.endGame()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).endGame();
    assertEquals(
        InitialPlayer.INITIATOR,
        sut.getGameField().getWinnerOrNull().getInitialPlayerInformation());
    assertEquals(currentPlayer, sut.getWinner());
  }

  @Test
  public void testHandleGameSyncEndGameRestartRound() {
    Player otherPlayer = new Player(2, InitialPlayer.OPPONENT);

    sut.setWhoAmI(InitialPlayer.INITIATOR);
    SyncRoot mockSyncRoot = mock(SyncRoot.class);
    GameField gameField = new GameField();
    sut.setGameField(gameField);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    currentPlayer.setHasLastPlayed(true);
    currentPlayer.setHasPassed(true);
    gameField.setOpponent(otherPlayer);
    gameField.getRows().getMeleeRowForP1().put("0_index", testCards.get(0));
    when(mockSyncRoot.getGameField()).thenReturn(gameField);
    when(mockGameStateMachine.getCurrent()).thenReturn(GameState.END_ROUND);
    when(mockGameStateMachine.restartRound()).thenReturn(true);

    sut.handleGameSyncUpdates(mockSyncRoot);

    verify(mockGameStateMachine).restartRound();
    assertFalse(currentPlayer.isHasLastPlayed());
    assertFalse(currentPlayer.isHasPassed());
    assertEquals(1, sut.getCardMulligansLeft());
  }

  @Test
  public void testGetCardsToMulligan() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    sut.setGameField(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);

    ArrayList<Card> cards = sut.getCardsToMulligan();

    assertEquals(6, cards.size());
  }

  @Test
  public void testGetCardsToMulliganForLaterRounds() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    currentPlayer.setCurrentMatchPoints(1);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);

    ArrayList<Card> cards = sut.getCardsToMulligan();

    assertEquals(3, cards.size());
  }

  @Test
  public void testMulliganCardsCountForLaterRounds() {
    sut.setWhoAmI(InitialPlayer.INITIATOR);
    GameField gameField = new GameField();
    ArrayList<Card> initialPlayerCards = playerCardsFrom(testCards);
    gameField.setPlayingCardsFor(InitialPlayer.INITIATOR, initialPlayerCards);
    gameField.getCurrentHandCardsFor(InitialPlayer.INITIATOR).remove("1_card");
    gameField.setCardDecks(cardDecks);
    gameField.setCurrentPlayer(currentPlayer);
    gameField.setOpponent(currentPlayer);
    currentPlayer.setCurrentMatchPoints(1);
    gameField.setCurrentPlayer(currentPlayer);
    sut.setGameField(gameField);
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(true);

    // normaly 1, but 3, because drawing 3 cards overflowed handCardsMaximum by 2
    assertEquals(3, sut.getCardMulligansLeft());
  }

  @Test
  public void testGetCardsToMulliganFailed() {
    when(mockGameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)).thenReturn(false);

    ArrayList<Card> cards = sut.getCardsToMulligan();

    assertNull(cards);
  }

  @Test
  public void getPlayerToTurnForRound0() {
    Player mockCurrentPlayer = mock(Player.class);
    Player mockOpponentPlayer = mock(Player.class);
    GameField gameField = mock(GameField.class);
    sut.setGameField(gameField);
    when(mockGameStateMachine.stateEquals(GameState.START_PLAYER_TURN)).thenReturn(true);
    when(gameField.getPlayer(eq(InitialPlayer.INITIATOR))).thenReturn(mockCurrentPlayer);
    when(gameField.getPlayer(eq(InitialPlayer.OPPONENT))).thenReturn(mockOpponentPlayer);

    ArrayList<MyTurnTestData> testData =
        new ArrayList<>(); // <RoundNumber, StartingPlayer.hasPlayed>
    testData.add(
        new MyTurnTestData(0, InitialPlayer.INITIATOR, false, InitialPlayer.INITIATOR, false));
    testData.add(
        new MyTurnTestData(0, InitialPlayer.INITIATOR, true, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(0, InitialPlayer.OPPONENT, false, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(0, InitialPlayer.OPPONENT, true, InitialPlayer.INITIATOR, false));

    testData.add(
        new MyTurnTestData(0, InitialPlayer.INITIATOR, false, InitialPlayer.OPPONENT, true));
    testData.add(
        new MyTurnTestData(0, InitialPlayer.OPPONENT, false, InitialPlayer.INITIATOR, true));

    for (MyTurnTestData data : testData) {
      sut.setStartingPlayer(data.startingPlayer);
      when(gameField.getRoundNumber()).thenReturn(data.round);

      if (data.startingPlayer == InitialPlayer.INITIATOR) {
        when(mockCurrentPlayer.isHasLastPlayed()).thenReturn(data.hasInitialPlayerPlayed);
        when(mockCurrentPlayer.isHasPassed()).thenReturn(data.playerHasPassed);
      } else {
        when(mockOpponentPlayer.isHasLastPlayed()).thenReturn(data.hasInitialPlayerPlayed);
        when(mockOpponentPlayer.isHasPassed()).thenReturn(data.playerHasPassed);
      }

      InitialPlayer playerToTurn = sut.getPlayerToTurn();

      assertEquals(data.expected, playerToTurn);
    }
  }

  @Test
  public void getPlayerToTurnForRound1And2() {
    Player mockCurrentPlayer = mock(Player.class);
    Player mockOpponentPlayer = mock(Player.class);
    GameField gameField = mock(GameField.class);
    sut.setGameField(gameField);
    when(mockGameStateMachine.stateEquals(GameState.START_PLAYER_TURN)).thenReturn(true);
    when(gameField.getPlayer(eq(InitialPlayer.INITIATOR))).thenReturn(mockCurrentPlayer);
    when(gameField.getPlayer(eq(InitialPlayer.OPPONENT))).thenReturn(mockOpponentPlayer);

    ArrayList<MyTurnTestData> testData =
        new ArrayList<>(); // <RoundNumber, StartingPlayer.hasPlayed>

    // startingPlayer will have won first round in getPlayersWonRound()
    testData.add(
        new MyTurnTestData(1, InitialPlayer.INITIATOR, false, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.INITIATOR, true, InitialPlayer.INITIATOR, false));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.INITIATOR, false, InitialPlayer.INITIATOR, true));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.INITIATOR, false, InitialPlayer.INITIATOR, true));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.OPPONENT, false, InitialPlayer.INITIATOR, false));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.OPPONENT, true, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.OPPONENT, false, InitialPlayer.OPPONENT, true));
    testData.add(
        new MyTurnTestData(1, InitialPlayer.OPPONENT, false, InitialPlayer.OPPONENT, true));

    testData.add(
        new MyTurnTestData(2, InitialPlayer.INITIATOR, false, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.INITIATOR, true, InitialPlayer.INITIATOR, false));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.INITIATOR, false, InitialPlayer.INITIATOR, true));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.INITIATOR, false, InitialPlayer.INITIATOR, true));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.OPPONENT, false, InitialPlayer.INITIATOR, false));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.OPPONENT, true, InitialPlayer.OPPONENT, false));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.OPPONENT, false, InitialPlayer.OPPONENT, true));
    testData.add(
        new MyTurnTestData(2, InitialPlayer.OPPONENT, false, InitialPlayer.OPPONENT, true));

    for (MyTurnTestData data : testData) {
      sut.setStartingPlayer(data.startingPlayer);
      when(gameField.getRoundNumber()).thenReturn(data.round);
      sut.setPlayersRoundsWon(data.getPlayersWonRound());

      if (data.startingPlayer.other()
          == InitialPlayer.INITIATOR) { // here other, because startingPlayer is swaped because of
        // getPlayersWonRound() mock
        when(mockCurrentPlayer.isHasLastPlayed()).thenReturn(data.hasInitialPlayerPlayed);
        when(mockCurrentPlayer.isHasPassed()).thenReturn(data.playerHasPassed);
      } else {
        when(mockOpponentPlayer.isHasLastPlayed()).thenReturn(data.hasInitialPlayerPlayed);
        when(mockOpponentPlayer.isHasPassed()).thenReturn(data.playerHasPassed);
      }

      InitialPlayer playerToTurn = sut.getPlayerToTurn();

      assertEquals(data.expected, playerToTurn);
    }
  }
  // Helper Methods

  private ArrayList<Card> playerCardsFrom(ArrayList<Card> cards) {
    ArrayList<Card> playerCards = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      playerCards.add(cards.get(i));
    }

    return playerCards;
  }

  private void setupTestCards() {
    for (int i = 1; i <= 20; i++) {
      testCards.add(
          new Card(
              i,
              "TestCard " + i,
              new ArrayList<CardType>(),
              i,
              0,
              "This is a test Card",
              new DeployTrigger(null, null),
              new OrderTrigger(null, null, 1, true),
              "hello",
              "world"));
    }
  }

  class MyTurnTestData {
    int round;
    InitialPlayer startingPlayer;
    boolean hasInitialPlayerPlayed;
    InitialPlayer expected;
    boolean playerHasPassed;

    public MyTurnTestData(
        int round,
        InitialPlayer startingPlayer,
        boolean hasInitialPlayerPlayed,
        InitialPlayer expected,
        boolean playerHasPassed) {
      this.round = round;
      this.startingPlayer = startingPlayer;
      this.hasInitialPlayerPlayed = hasInitialPlayerPlayed;
      this.expected = expected;
      this.playerHasPassed = playerHasPassed;
    }

    public ArrayList<InitialPlayer> getPlayersWonRound() {
      ArrayList<InitialPlayer> winList = new ArrayList<>();
      if (round == 1) {
        winList.add(startingPlayer);
      }
      if (round == 2) {
        winList.add(startingPlayer.other());
        winList.add(startingPlayer);
      }
      return winList;
    }
  }
}
