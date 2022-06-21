package at.aau.se2.gamelogic;

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.util.Log;
import androidx.annotation.Nullable;
import at.aau.se2.gamelogic.comunication.CommuncationObserver;
import at.aau.se2.gamelogic.comunication.FirebaseConnector;
import at.aau.se2.gamelogic.comunication.Result;
import at.aau.se2.gamelogic.comunication.ResultObserver;
import at.aau.se2.gamelogic.comunication.SyncAction;
import at.aau.se2.gamelogic.comunication.SyncRoot;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gamelogic.models.Hero;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.RowStatus;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetRowAction;
import at.aau.se2.gamelogic.models.cardactions.actions.TargetUnitAction;
import at.aau.se2.gamelogic.models.cardactions.triggers.DeployTrigger;
import at.aau.se2.gamelogic.models.cardactions.triggers.OrderTrigger;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gamelogic.util.SyncActionUtil;

// TODO: Handle Card Actions
// TODO: SyncAction for CardActions (opt)

public class GameLogic {
  public static final int ROW_CARD_NUMBER = 10;
  public static final int HAND_CARD_NUMBER = 10;
  public static final int STATUS_DURATION = 2;
  private static final String TAG = GameLogic.class.getSimpleName();
  private int gameId = -1;
  private GameField gameField;
  private InitialPlayer whoAmI;
  private FirebaseConnector connector;
  private GameStateMachine gameStateMachine = new GameStateMachine();
  private InitialPlayer startingPlayer;
  private int cardMulligansLeft = 3;
  // set when playing card or using hero activity
  private boolean currentPlayerCanPass = true;
  private HashMap<InitialPlayer, Boolean> playerHasMulliganedCards = new HashMap<>();
  private int lastSavedActionSize = 0;
  private ArrayList<InitialPlayer> playersRoundsWon = new ArrayList<>();
  private ArrayList<RowStatus> statuses =
      new ArrayList<>(Arrays.asList(RowStatus.FOG, RowStatus.RAIN, RowStatus.FROST));

  @Nullable private GameLogicDataProvider gameLogicDataProvider;

  private ArrayList<UIActionListener> uiActionListenerArrayList = new ArrayList<>();
  private ArrayList<GameFieldObserver> gameFieldObservers = new ArrayList<>();
  private CommuncationObserver communcationObserver =
      sync -> {
        handleGameSyncUpdates(sync);

        for (GameFieldObserver observers : gameFieldObservers) {
          observers.updateGameField(sync.getGameField());
        }
      };

  public GameLogic(
      @Nullable FirebaseConnector connector, @Nullable GameStateMachine gameStateMachine) {
    this.connector = (connector != null) ? connector : new FirebaseConnector();
    this.gameStateMachine = (gameStateMachine != null) ? gameStateMachine : new GameStateMachine();
    this.connector.addListener(communcationObserver);

    playerHasMulliganedCards.put(InitialPlayer.INITIATOR, false);
    playerHasMulliganedCards.put(InitialPlayer.OPPONENT, false);
  }

  // GameState Manipulation

  public void startGame(ResultObserver<Integer, Error> observer) {
    if (!gameStateMachine.canProgressTo(GameState.WAIT_FOR_OPPONENT)) {
      observer.finished(Result.Failure(new Error("Game already started.")));
      return;
    }

    connector.createGame(
        new ResultObserver<Integer, Error>() {
          @Override
          public void finished(Result<Integer, Error> result) {
            if (result.isSuccessful()) {
              if (!gameStateMachine.startGame()) {
                observer.finished(Result.Failure(new Error("Game already started.")));
                return;
              }
              gameId = result.getValue();
              whoAmI = InitialPlayer.INITIATOR;

              initializeGame(
                  new Player(1, InitialPlayer.INITIATOR),
                  new CardDecks(new ArrayList<>(), new ArrayList<>()),
                  new ArrayList<>());
            }

            observer.finished(result);
          }
        });
  }

  public void joinGame(int id, ResultObserver<GameField, Error> observer) {
    if (!gameStateMachine.canProgressTo(GameState.START_GAME_ROUND)) {
      observer.finished(Result.Failure(new Error("Game already started.")));
      return;
    }

    connector.joinGame(
        id,
        new ResultObserver<GameField, Error>() {
          @Override
          public void finished(Result<GameField, Error> result) {
            if (result.isSuccessful()) {
              if (!gameStateMachine.joinGame()) {
                observer.finished(Result.Failure(new Error("Game already started.")));
                return;
              }

              gameId = id;
              gameField = result.getValue();
              gameField.setOpponent(new Player(2, InitialPlayer.OPPONENT));
              connector.syncGameField(gameField);

              whoAmI = InitialPlayer.OPPONENT;
            }

            observer.finished(result);
          }
        });
  }

  private void chooseCardDeck(ArrayList<Card> cards) {
    gameField.setCardDeckFor(whoAmI, cards);
    connector.syncGameField(gameField);
  }

  /*
   @return Returns cardId to show instead of old card
  */
  public @Nullable String mulliganCard(int cardId) {
    if (!gameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)) return null;
    if (cardMulligansLeft == 0) {
      Log.w(TAG, "There are no mulligans left this round");
      return null;
    }

    ArrayList<Card> cards = new ArrayList<Card>(gameField.getCardDeck(whoAmI).values());
    HashMap<String, Card> playingCards = gameField.getCurrentHandCardsFor(whoAmI);

    int startingPlayingCardsSize = playingCards.size();
    playingCards.remove(cardId + "_card");

    String mulliganedCardId = null;
    Random random = new Random();
    // try to change card, until we have same amount again
    while (playingCards.size() < startingPlayingCardsSize) {
      int randomIndex = random.nextInt(cards.size());
      Card card = cards.get(randomIndex);

      // card already in playing cards, try again
      if (playingCards.containsKey(card.getFirebaseId())) continue;

      mulliganedCardId = card.getFirebaseId();
      playingCards.put(mulliganedCardId, card);
    }

    gameField.setPlayingCardsFor(whoAmI, new ArrayList<>(playingCards.values()));
    connector.syncGameField(gameField);
    cardMulligansLeft -= 1;

    if (cardMulligansLeft == 0) {
      connector.sendSyncAction(new SyncAction(SyncAction.Type.MULLIGAN_COMPLETE, whoAmI.name()));
    }

    return mulliganedCardId;
  }

  // TODO send same syncAction with Vibration Type
  public void abortMulliganCards() {
    cardMulligansLeft = 0;
    connector.sendSyncAction(new SyncAction(SyncAction.Type.MULLIGAN_COMPLETE, whoAmI.name()));
  }

  public void endTurn() {
    if (!gameStateMachine.stateEquals(GameState.START_PLAYER_TURN)) {
      Log.w(TAG, "Wrong state to end round");
      return;
    }

    if (!isMyTurn()) {
      Log.w(TAG, "It is not your turn, cannot end turn");
      return;
    }

    gameField.getPlayer(whoAmI).setHasLastPlayed(true);
    connector.syncGameField(gameField);
  }

  public void pass() {
    if (!gameStateMachine.stateEquals(GameState.START_PLAYER_TURN)) {
      Log.w(TAG, "Wrong state to pass round");
      return;
    }

    if (!isMyTurn()) {
      Log.w(TAG, "It is not your turn, cannot end turn");
      return;
    }

    if (!currentPlayerCanPass) return;

    gameField.getPlayer(whoAmI).setHasPassed(true);
    connector.syncGameField(gameField);
  }

  private void firstGameSetup() {
    Random random = new Random();
    InitialPlayer startingPlayer =
        (random.nextInt(2) == 1) ? InitialPlayer.INITIATOR : InitialPlayer.OPPONENT;

    connector.sendSyncAction(
        new SyncAction(SyncAction.Type.STARTING_PLAYER, startingPlayer.name()));
  }

  private void initializeGame(Player currentPlayer, CardDecks cardDecks, ArrayList<Hero> heroes) {
    if (!gameStateMachine.stateEquals(GameState.WAIT_FOR_OPPONENT)) {
      return;
    }

    gameField = new GameField(new GameFieldRows(), currentPlayer, null, cardDecks, heroes);

    connector.syncGameField(gameField);
  }

  private void drawCards() {
    ArrayList<Card> cards = new ArrayList<Card>(gameField.getCardDeck(whoAmI).values());
    if (cards.size() < HAND_CARD_NUMBER) {
      Log.w(TAG, "CardDecks not setup");
      return;
    }

    Random random = new Random();
    // 10 random unique cards from set cardDecks
    HashMap<Integer, Card> drawnCards = new HashMap<>();
    while (drawnCards.size() < HAND_CARD_NUMBER) {
      int randomIndex = random.nextInt(cards.size());
      Card card = cards.get(randomIndex);
      drawnCards.put(card.getId(), card);
    }

    gameField.setPlayingCardsFor(whoAmI, new ArrayList<>(drawnCards.values()));
  }

  // Need to do it person per person to prevent race-condidition, where both players
  // update gamefield and delete others player input
  private void requestCardDeck() {
    boolean initiatorSet = gameField.getCardDeck(InitialPlayer.INITIATOR).size() > 0;

    if (!initiatorSet && whoAmI == InitialPlayer.INITIATOR) {
      if (gameLogicDataProvider == null) return;
      ArrayList cardDeck = gameLogicDataProvider.needsCardDeck();
      if (cardDeck == null) return;
      chooseCardDeck(cardDeck);
    }

    if (initiatorSet && whoAmI == InitialPlayer.OPPONENT) {
      if (gameLogicDataProvider == null) return;
      ArrayList cardDeck = gameLogicDataProvider.needsCardDeck();
      if (cardDeck == null) return;
      chooseCardDeck(cardDeck);
    }
  }

  private void turnReset() {
    gameField.getCurrentPlayer().setHasLastPlayed(false);
    gameField.getOpponent().setHasLastPlayed(false);
  }

  private void roundReset() {
    turnReset();
    gameField.getCurrentPlayer().setHasPassed(false);
    gameField.getOpponent().setHasPassed(false);
    cardMulligansLeft = 1;
    playerHasMulliganedCards.put(InitialPlayer.INITIATOR, false);
    playerHasMulliganedCards.put(InitialPlayer.OPPONENT, false);
  }

  // checks curent game state + more
  protected void handleGameSyncUpdates(SyncRoot syncRoot) {
    if (syncRoot == null) return;

    this.gameField = syncRoot.getGameField();

    ArrayList<SyncAction> newSyncActions = syncRoot.getLastActions(lastSavedActionSize);

    switch (gameStateMachine.getCurrent()) {
      case WAIT_FOR_OPPONENT:
        if (syncRoot.getGameField() != null && syncRoot.getGameField().getOpponent() != null) {
          // Somebody joined the game
          gameStateMachine.opponentJoined();
          firstGameSetup();
        }
        break;

      case START_GAME_ROUND:
        if (gameField == null) return;

        requestCardDeck();
        if (!gameField.getCardDecks().hasDecksForBothPlayers()) {
          Log.w(TAG, "Only one Player has choosen his deck");
          return;
        }
        InitialPlayer startingPlayer = SyncActionUtil.findStartingPlayer(newSyncActions);
        if (gameStateMachine.roundCanStart()) {
          lastSavedActionSize = syncRoot.getSyncActions().size();
          this.startingPlayer = startingPlayer;
        }
        break;

      case DRAW_CARDS:
        if (gameField == null || gameField.getCurrentPlayer() == null) return;
        drawCards();

        if (gameStateMachine.cardsDrawn()) {
          connector.syncGameField(this.gameField);
        }
        break;

      case MULLIGAN_CARDS:
        InitialPlayer mulliganedPlayer = SyncActionUtil.findPlayerHasMulliganed(newSyncActions);
        if (mulliganedPlayer == null) return;
        playerHasMulliganedCards.put(mulliganedPlayer, true);
        lastSavedActionSize = syncRoot.getSyncActions().size();

        if (!bothPlayerHaveMulliganed()) return;
        gameStateMachine.cardsChanged();
        gameField.updateGameDecksAfterMulligan();
        break;

      case START_PLAYER_TURN:
        updateBoardState();

        InitialPlayer playerToVibrate = SyncActionUtil.findVibrationOn2ndDevice(newSyncActions);
        if (playerToVibrate != null) {
          lastSavedActionSize = syncRoot.getSyncActions().size();
          if (whoAmI == playerToVibrate) {
            notifyVibrationActionListener();
          }
        }

        if (!bothPlayerHavePlayed()) {
          Log.w(TAG, "Not both players played");
          return;
        }

        if (gameStateMachine.endPlayerTurns()) handleGameSyncUpdates(syncRoot);
        break;

      case END_PLAYER_TURN:
        if (bothPLayerHavePassed()) {
          Log.i(TAG, "Both players passed, round ends.");
          if (gameStateMachine.endRound()) handleGameSyncUpdates(syncRoot);
          return;
        }

        if (bothPlayerOutOfCards()) {
          Log.i(TAG, "Both players run out of cards, round ends.");
          if (gameStateMachine.endRound()) handleGameSyncUpdates(syncRoot);
          return;
        }

        turnReset();

        if (gameStateMachine.restartTurns()) {
          connector.syncGameField(this.gameField);
        }
        break;

      case END_ROUND:
        // TODO: Refactor to make sure, Initatior and Oponent syncing tasks get clearer

        Player player = this.gameField.getPointLeadingPlayer();
        // only game initiator can randomly choose winner
        if (player == null && whoAmI == InitialPlayer.INITIATOR) {
          // round tied, choose random winner
          Random random = new Random();
          InitialPlayer randomWinningPlayer =
              (random.nextInt(2) == 1) ? InitialPlayer.INITIATOR : InitialPlayer.OPPONENT;
          player = this.gameField.getPlayer(randomWinningPlayer);
          connector.sendSyncAction(
              new SyncAction(
                  SyncAction.Type.ROUND_WINNER, player.getInitialPlayerInformation().name()));
        }

        if (player == null && whoAmI == InitialPlayer.OPPONENT) {
          InitialPlayer syncedWinningPlayer = SyncActionUtil.findWinningPlayer(newSyncActions);
          if (syncedWinningPlayer == null) return;
          lastSavedActionSize = syncRoot.getSyncActions().size();
          player = this.gameField.getPlayer(syncedWinningPlayer);
        }

        if (player != null) {
          addWinner(player.getInitialPlayerInformation());
          player.setCurrentMatchPoints(player.getCurrentMatchPoints() + 1);
        }

        Player winningPlayer = this.gameField.getWinnerOrNull();
        if (winningPlayer != null) {
          Log.i(TAG, winningPlayer.getInitialPlayerInformation() + " has won");
          if (gameStateMachine.endGame()) {
            if (whoAmI == InitialPlayer.INITIATOR) connector.syncGameField(this.gameField);
          }
          return;
        }

        if (player == null) return;
        if (gameStateMachine.restartRound()) {
          roundReset();
          connector.syncGameField(this.gameField);
        }
        // TODO: clean gameboard
        break;

      default:
        break;
    }
  }

  // Card Actions

  /**
   * Performs the deploy trigger of the a card (if card has a deploy trigger).
   *
   * @param card the card for which the deploy trigger should be executed.
   */
  private void performDeployTrigger(Card card) {
    DeployTrigger deployTrigger = card.getDeployTrigger();
    if (deployTrigger == null) {
      return;
    }
    performTargetRowActions(deployTrigger.getTargetRowActions());
    performTargetUnitActions(deployTrigger.getTargetUnitActions());
  }

  /** @param card the card for which the order trigger should be executed. */
  public void performOrderTrigger(Card card) {
    OrderTrigger orderTrigger = card.getOrderTrigger();
    if (orderTrigger == null) {
      return;
    }
    if (orderTrigger.getRemCoolDown() == 0) {
      performTargetRowActions(orderTrigger.getTargetRowActions());
      performTargetUnitActions(orderTrigger.getTargetUnitActions());

      orderTrigger.setRemCoolDown(orderTrigger.getCoolDown());
    } else {
      Log.w(TAG, "Order trigger can't be activated due to remaining cooldown.");
    }
  }

  /** This function gets called from the updateBoardState function at the beginning of a turn. */
  private void updateOrderRemCoolDown(Card card) {
    OrderTrigger orderTrigger = card.getOrderTrigger();
    if (orderTrigger == null) {
      return;
    }
    int remCoolDown = orderTrigger.getRemCoolDown();
    if (remCoolDown > 0) {
      orderTrigger.setRemCoolDown(remCoolDown - 1);
    }
  }

  /** @param targetRowActions */
  private void performTargetRowActions(ArrayList<TargetRowAction> targetRowActions) {
    InitialPlayer opponent = whoAmI.other();

    for (TargetRowAction targetRowAction : targetRowActions) {
      Row targetRow = null;

      // determine targeted rows
      if (targetRowAction.hasRandomTargets()) {
        Random rand = new Random();
        int n = rand.nextInt() % 2;
        if (n == 0) {
          targetRow = gameField.getRows().getMeleeRowFor(opponent);
        } else {
          targetRow = gameField.getRows().getRangedRowFor(opponent);
        }
      } else {
        // all our cards have random targets, but this branch could be used in the future to extend
        // functionality
      }

      // determine status to apply
      RowStatus status = null;
      if (targetRowAction.hasRandomStatus()) {
        Collections.shuffle(statuses);
        status = statuses.get(0);
      } else {
        // all our cards apply random statuses, but this branch could be used in the future to
        // extend functionality
      }

      // apply status
      if (targetRow != null && status != null) {
        targetRow.setRowStatus(status);
        targetRow.setRemainingStatusRounds(STATUS_DURATION);
      }
    }
  }

  /**
   * Executes all targetUnitActions (i.e. DAMAGE, HEAL, BOOST, SWAP) on random targets or targets
   * chosen by the executing player.
   *
   * @param targetUnitActions List of actions to execute.
   */
  private void performTargetUnitActions(ArrayList<TargetUnitAction> targetUnitActions) {
    InitialPlayer initiator = whoAmI;
    InitialPlayer opponent = whoAmI.other();

    for (TargetUnitAction targetUnitAction : targetUnitActions) {
      // determine targeted units
      ArrayList<Card> targetedCards = new ArrayList<>();
      if (targetUnitAction.hasRandomTargets()) {
        ArrayList<Card> legalTargets = new ArrayList<>();
        if (targetUnitAction.canTargetAlliedUnits() && targetUnitAction.canTargetEnemyUnits()) {
          legalTargets.addAll(gameField.getRows().meleeRowFor(initiator).values());
          legalTargets.addAll(gameField.getRows().rangedRowFor(initiator).values());
          legalTargets.addAll(gameField.getRows().meleeRowFor(opponent).values());
          legalTargets.addAll(gameField.getRows().rangedRowFor(opponent).values());
        } else if (targetUnitAction.canTargetAlliedUnits()) {
          legalTargets.addAll(gameField.getRows().meleeRowFor(initiator).values());
          legalTargets.addAll(gameField.getRows().rangedRowFor(initiator).values());
        } else {
          legalTargets.addAll(gameField.getRows().meleeRowFor(opponent).values());
          legalTargets.addAll(gameField.getRows().rangedRowFor(opponent).values());
        }
        Collections.shuffle(legalTargets);

        for (int i = 0; i < targetUnitAction.getNumTargets() && i < legalTargets.size(); i++) {
          targetedCards.add(legalTargets.get(i));
        }

        ArrayList<Integer> targetedCardsUUIDs = new ArrayList();
        for (Card card : targetedCards) {
          targetedCardsUUIDs.add(card.getId());
        }
        targetUnitAction.setTargetedCardsUUIDs(targetedCardsUUIDs);

      } else {
        // TODO: select targets by hand (currently not possible due to missing selectCard within UI)
      }

      // determine action to take and execute it
      for (Card card : targetedCards) {
        switch (targetUnitAction.getActionType()) {
          case DAMAGE:
            damageTargetCard(card, targetUnitAction);
            break;
          case HEAL:
            healTargetCard(card, targetUnitAction);
            break;
          case BOOST:
            boostTargetCard(card, targetUnitAction);
            break;
          case SWAP:
            swapTargetCard(card, targetUnitAction);
            break;
        }
      }
    }
  }

  /**
   * Damage the targeted card. Afterwards the checkForDestroyedCards function is called to remove
   * destroyed cards from the board.
   *
   * @param targetCard
   * @param damageAction
   */
  private void damageTargetCard(Card targetCard, TargetUnitAction damageAction) {
    targetCard.setPower(targetCard.getPower() - damageAction.getPoints());
    targetCard.setPowerDiff(targetCard.getPowerDiff() - damageAction.getPoints());

    checkForDestroyedCards();
  }

  /**
   * Heal the targeted card. A heal can't extend the cards base power. If a card is damaged
   * powerDiff is set to a neg. value. We have to ensure that a heal with points greater than the
   * current damage points on the card doesn't extend the cards initial power.
   *
   * @param targetCard
   * @param healAction
   */
  private void healTargetCard(Card targetCard, TargetUnitAction healAction) {
    if (targetCard.getPowerDiff() < 0) {
      if ((targetCard.getPowerDiff() + healAction.getPoints()) <= 0) {
        targetCard.setPower(targetCard.getPower() + healAction.getPoints());
        targetCard.setPowerDiff(targetCard.getPowerDiff() + healAction.getPoints());
      } else {
        /*
         * here we subtract, because powerDiff is neg. when the card is dmg. (results in
         * increasing the cards power to it's initial power)
         */
        targetCard.setPower(targetCard.getPower() - targetCard.getPowerDiff());
        targetCard.setPowerDiff(0);
      }
    }
  }

  /**
   * Boost the targeted card. A boost can extend the cards base power. If a card has more power than
   * it's initial power, powerDiff is set to a positive value.
   *
   * @param targetCard
   * @param boostAction
   */
  private void boostTargetCard(Card targetCard, TargetUnitAction boostAction) {
    targetCard.setPower(targetCard.getPower() + boostAction.getPoints());
    targetCard.setPowerDiff(targetCard.getPowerDiff() + boostAction.getPoints());
  }

  /**
   * Moves the targeted card to the other row (MELEE -> RANGED, RANGED -> MELEE) if possible.
   *
   * @param targetCard
   * @param swapAction
   */
  private void swapTargetCard(Card targetCard, TargetUnitAction swapAction) {
    HashMap<String, Card> p1MeleeRow = gameField.getRows().getMeleeRowForP1();
    HashMap<String, Card> p1RangedRow = gameField.getRows().getRangeRowForP1();
    HashMap<String, Card> p2MeleeRow = gameField.getRows().getMeleeRowForP2();
    HashMap<String, Card> p2RangedRow = gameField.getRows().getRangeRowForP2();

    // swaps between p1 rows
    if (p1MeleeRow.containsKey(targetCard.getFirebaseId())) {
      if (p1RangedRow.size() < ROW_CARD_NUMBER) {
        p1RangedRow.put(targetCard.getFirebaseId(), targetCard);
        p1MeleeRow.remove(targetCard.getFirebaseId());
      }
    } else if (p1RangedRow.containsKey(targetCard.getFirebaseId())) {
      if (p1MeleeRow.size() < ROW_CARD_NUMBER) {
        p1MeleeRow.put(targetCard.getFirebaseId(), targetCard);
        p1RangedRow.remove(targetCard.getFirebaseId());
      }
    }

    // swaps between p2 rows
    if (p2MeleeRow.containsKey(targetCard.getFirebaseId())) {
      if (p2RangedRow.size() < ROW_CARD_NUMBER) {
        p2RangedRow.put(targetCard.getFirebaseId(), targetCard);
        p2MeleeRow.remove(targetCard.getFirebaseId());
      }
    } else if (p2RangedRow.containsKey(targetCard.getFirebaseId())) {
      if (p2MeleeRow.size() < ROW_CARD_NUMBER) {
        p2MeleeRow.put(targetCard.getFirebaseId(), targetCard);
        p2RangedRow.remove(targetCard.getFirebaseId());
      }
    }
  }

  /**
   * check if the power of a card on the board is currently <= 0 if yes the card is removed from the
   * board.
   */
  private void checkForDestroyedCards() {
    HashMap<String, Card> p1MeleeRow = gameField.getRows().getMeleeRowForP1();
    HashMap<String, Card> p1RangedRow = gameField.getRows().getRangeRowForP1();
    HashMap<String, Card> p2MeleeRow = gameField.getRows().getMeleeRowForP2();
    HashMap<String, Card> p2RangedRow = gameField.getRows().getRangeRowForP2();

    ArrayList<String> destroyedCardsUUIDs = new ArrayList<>();
    for (Card card : p1MeleeRow.values()) {
      if (card.getPower() <= 0) {
        destroyedCardsUUIDs.add(card.getFirebaseId());
      }
    }
    for (Card card : p1RangedRow.values()) {
      if (card.getPower() <= 0) {
        destroyedCardsUUIDs.add(card.getFirebaseId());
      }
    }
    for (Card card : p2MeleeRow.values()) {
      if (card.getPower() <= 0) {
        destroyedCardsUUIDs.add(card.getFirebaseId());
      }
    }
    for (Card card : p2RangedRow.values()) {
      if (card.getPower() <= 0) {
        destroyedCardsUUIDs.add(card.getFirebaseId());
      }
    }

    for (String cardUUID : destroyedCardsUUIDs) {
      p1MeleeRow.remove(cardUUID);
      p1RangedRow.remove(cardUUID);
      p2MeleeRow.remove(cardUUID);
      p2RangedRow.remove(cardUUID);
    }
  }

  /**
   * This function gets called from the updateBoardState function at the beginning of a turn. checks
   * if a row has currently a status applied on it if yes perform status action on row and decrement
   * remRounds by 1. if remRounds reaches 0 remove the status from the row.
   */
  private void performRowStatusAction() {
    InitialPlayer currentPlayer = getPlayerToTurn();

    ArrayList<Row> rows = new ArrayList<>();
    rows.add(gameField.getRows().getMeleeRowFor(currentPlayer));
    rows.add(gameField.getRows().getRangedRowFor(currentPlayer));
    for (Row row : rows) {
      RowStatus status = row.getRowStatus();
      if (status != null) {
        ArrayList<Card> currentCardsOnRow = new ArrayList<>(row.getPlayerRow().values());
        switch (status) {
          case FOG:
            if (currentCardsOnRow.size() > 0) {
              Card maxPowerCard = currentCardsOnRow.get(0);
              for (int i = 1; i < currentCardsOnRow.size(); i++) {
                Card currentCard = currentCardsOnRow.get(i);
                if (currentCard.getPower() > maxPowerCard.getPower()) {
                  maxPowerCard = currentCard;
                }
              }

              maxPowerCard.setPower(maxPowerCard.getPower() - 2);
              maxPowerCard.setPowerDiff(maxPowerCard.getPowerDiff() - 2);
            }
            break;
          case FROST:
            if (currentCardsOnRow.size() > 0) {
              Card minPowerCard = currentCardsOnRow.get(0);
              for (int i = 1; i < currentCardsOnRow.size(); i++) {
                Card currentCard = currentCardsOnRow.get(i);
                if (currentCard.getPower() < minPowerCard.getPower()) {
                  minPowerCard = currentCard;
                }
              }

              minPowerCard.setPower(minPowerCard.getPower() - 2);
              minPowerCard.setPowerDiff(minPowerCard.getPowerDiff() - 2);
            }
            break;
          case RAIN:
            if (currentCardsOnRow.size() > 0) {
              Collections.shuffle(currentCardsOnRow);

              Card randomCardOnRow = currentCardsOnRow.get(0);
              randomCardOnRow.setPower(randomCardOnRow.getPower() - 2);
              randomCardOnRow.setPowerDiff(randomCardOnRow.getPowerDiff() - 2);
            }
        }
        row.setRemainingStatusRounds(row.getRemainingStatusRounds() - 1);
        if (row.getRemainingStatusRounds() == 0) {
          row.setRowStatus(null);
        }

        checkForDestroyedCards();
      }
    }
  }

  private void updateBoardState() {
    performRowStatusAction();

    InitialPlayer currentPlayer = getPlayerToTurn();
    HashMap<String, Card> currentPlayerMeleeRow = gameField.getRows().meleeRowFor(currentPlayer);
    HashMap<String, Card> currentPlayerRangedRow = gameField.getRows().rangedRowFor(currentPlayer);
    for (Card card : currentPlayerMeleeRow.values()) {
      updateOrderRemCoolDown(card);
    }
    for (Card card : currentPlayerRangedRow.values()) {
      updateOrderRemCoolDown(card);
    }
  }

  /**
   * 1) Deploy card on target row. 2) Remove card from hand. 3) Perform deploy trigger.
   *
   * @param card The card which should be put on the given row.
   * @param rowType Type of row where the card should get deployed.
   * @param position The position on the given row where the card is put.
   */
  public void deployCard(Card card, RowType rowType, int position) {
    InitialPlayer currentPlayer = getPlayerToTurn();

    gameField.getRows().setCardIfPossible(currentPlayer, rowType, position, card);

    HashMap<String, Card> currentHand = gameField.getCurrentHandCardsFor(currentPlayer);
    String key = card.getFirebaseId();
    currentHand.remove(key);

    currentPlayerCanPass = false;

    performDeployTrigger(card);

    sendVibrationAction();

    connector.syncGameField(this.gameField);
  }

  public void registerGameFieldListener(GameFieldObserver observer) {
    if (gameFieldObservers.contains(observer)) return;
    gameFieldObservers.add(observer);
    observer.updateGameField(gameField);
  }

  public void registeruiActionListener(UIActionListener uiListener) {
    if (uiActionListenerArrayList.contains(uiListener)) return;
    uiActionListenerArrayList.add(uiListener);
  }

  public ArrayList<Card> getCardsToMulligan() {
    if (!gameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)) return null;

    ArrayList<Card> cardsToMulligan = new ArrayList<>();
    ArrayList<Card> playingCards =
        new ArrayList<>(gameField.getCurrentHandCardsFor(whoAmI).values());
    int cardCountToMulligan = 3;
    if (gameField.getRoundNumber() == 0) cardCountToMulligan = 6;

    for (int i = 0; i < cardCountToMulligan; i++) {
      cardsToMulligan.add(playingCards.get(i));
    }

    return cardsToMulligan;
  }

  public InitialPlayer getPlayerToTurn() {
    if (!gameStateMachine.stateEquals(GameState.START_PLAYER_TURN)) return startingPlayer;

    int roundNumber = gameField.getRoundNumber(); // 0, 1, or 2

    InitialPlayer playerWonLastRound = null;
    if (playersRoundsWon.size() > max(0, roundNumber - 1)) {
      playerWonLastRound = playersRoundsWon.get(max(0, roundNumber - 1));
    }

    InitialPlayer startingPlayer =
        (playerWonLastRound == null) ? this.startingPlayer : playerWonLastRound.other();
    InitialPlayer otherPlayer = startingPlayer.other();

    if (gameField.getPlayer(startingPlayer).isHasPassed()) return otherPlayer;

    if (gameField.getPlayer(startingPlayer).isHasLastPlayed()) {
      return otherPlayer;
    } else {
      return startingPlayer;
    }
  }

  public boolean isMyTurn() {
    InitialPlayer playerToTurn = getPlayerToTurn();
    if (whoAmI == null || playerToTurn == null) return false;
    return playerToTurn.name().equals(whoAmI.name());
  }

  public @Nullable Player getWinner() {
    return gameField.getWinnerOrNull();
  }

  private void addWinner(InitialPlayer player) {
    playersRoundsWon.add(player);
  }

  private boolean bothPlayerHaveMulliganed() {
    return playerHasMulliganedCards.get(InitialPlayer.INITIATOR)
        && playerHasMulliganedCards.get(InitialPlayer.OPPONENT);
  }

  private boolean bothPlayerOutOfCards() {
    CardDecks playingCards = gameField.getCurrentHandCards();

    return playingCards.getP1Deck().isEmpty() && playingCards.getP2Deck().isEmpty();
  }

  private boolean bothPlayerHavePlayed() {
    Player currentPlayer = gameField.getCurrentPlayer();
    Player opponent = gameField.getOpponent();
    return (currentPlayer.isHasLastPlayed() || currentPlayer.isHasPassed())
        && (opponent.isHasLastPlayed() || opponent.isHasPassed());
  }

  private boolean bothPLayerHavePassed() {
    Player currentPlayer = gameField.getCurrentPlayer();
    Player opponent = gameField.getOpponent();
    return currentPlayer.isHasPassed() && opponent.isHasPassed();
  }

  private void sendVibrationAction() {
    notifyVibrationActionListener();
    connector.sendSyncAction(new SyncAction(SyncAction.Type.VIBRATION, whoAmI.other().name()));
  }

  private void notifyVibrationActionListener() {
    for (UIActionListener actionListener : uiActionListenerArrayList) {
      actionListener.sendVibration();
    }
  }

  // Getters & Setters

  public int getGameId() {
    return gameId;
  }

  public InitialPlayer getWhoAmI() {
    return whoAmI;
  }

  public GameFieldRows getGameFieldRows() {
    return gameField.getRows();
  }

  private Player getOpponent() {
    if (gameField.getCurrentPlayer().getInitialPlayerInformation() == InitialPlayer.INITIATOR) {
      return gameField.getOpponent();
    }
    return gameField.getCurrentPlayer();
  }

  protected void setGameField(GameField gameField) {
    this.gameField = gameField;
  }

  public GameField getGameField() {
    return gameField;
  }

  public GameStateMachine getGameStateMachine() {
    return gameStateMachine;
  }

  public GameState getCurrentGameState() {
    return gameStateMachine.getCurrent();
  }

  public InitialPlayer getStartingPlayer() {
    return startingPlayer;
  }

  protected void setStartingPlayer(InitialPlayer player) {
    this.startingPlayer = player;
  }

  protected void setWhoAmI(InitialPlayer player) {
    this.whoAmI = player;
  }

  public int getCardMulligansLeft() {
    return cardMulligansLeft;
  }

  protected void setCardMulligansLeft(int left) {
    this.cardMulligansLeft = left;
  }

  public void setGameLogicDataProvider(GameLogicDataProvider gameLogicDataProvider) {
    this.gameLogicDataProvider = gameLogicDataProvider;
  }

  protected void setCurrentPlayerCanPass(boolean currentPlayerCanPass) {
    this.currentPlayerCanPass = currentPlayerCanPass;
  }

  public boolean getCurrentPlayerCanPass() {
    return currentPlayerCanPass;
  }

  protected void setPlayersRoundsWon(ArrayList<InitialPlayer> playersRoundsWon) {
    this.playersRoundsWon = playersRoundsWon;
  }
}
