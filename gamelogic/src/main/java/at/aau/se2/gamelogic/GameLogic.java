package at.aau.se2.gamelogic;

import static java.lang.Math.max;

import java.util.ArrayList;
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
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.FogParams;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gamelogic.util.SyncActionUtil;

// TODO: Handle Card Actions
// TODO: SyncAction for CardActions (opt)

public class GameLogic {
  private static final String TAG = GameLogic.class.getSimpleName();
  private int gameId = -1;
  private GameField gameField;
  private InitialPlayer whoAmI;
  private FirebaseConnector connector;
  private ArrayList<CardActionCallback> cardActionCallbacks = new ArrayList<>();
  private GameStateMachine gameStateMachine = new GameStateMachine();
  private InitialPlayer startingPlayer;
  private int cardMulligansLeft = 3;
  // set when playing card or using hero activity
  private boolean currentPlayerCanPass = true;
  private HashMap<InitialPlayer, Boolean> playerHasMulliganedCards = new HashMap<>();
  private int lastSavedActionSize = 0;
  private ArrayList<InitialPlayer> playersRoundsWon = new ArrayList<>();

  @Nullable private GameLogicDataProvider gameLogicDataProvider;

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

  public void registerCardActionCallback(CardActionCallback callback) {
    if (cardActionCallbacks.contains(callback)) {
      return;
    }
    cardActionCallbacks.add(callback);
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
  public @Nullable Card mulliganCard(int cardId) {
    if (!gameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)) return null;
    if (cardMulligansLeft == 0) {
      Log.w(TAG, "There are no mulligans left this round");
      return null;
    }

    ArrayList<Card> cards = new ArrayList<Card>(gameField.getCardDeck(whoAmI).values());
    HashMap<String, Card> playingCards = gameField.getCurrentHandCardsFor(whoAmI);

    int startingPlayingCardsSize = playingCards.size();
    playingCards.remove(cardId + "_card");

    Card mulliganedCard = null;
    Random random = new Random();
    // try to change card, until we have same amount again
    while (playingCards.size() < startingPlayingCardsSize) {
      int randomIndex = random.nextInt(cards.size());
      Card card = cards.get(randomIndex);

      // card already in playing cards, try again
      if (playingCards.containsKey(card.getFirebaseId())) continue;

      mulliganedCard = card;
      playingCards.put(mulliganedCard.getFirebaseId(), card);
    }

    gameField.setPlayingCardsFor(whoAmI, new ArrayList<>(playingCards.values()));
    connector.syncGameField(gameField);
    cardMulligansLeft -= 1;

    if (cardMulligansLeft == 0) {
      connector.sendSyncAction(new SyncAction(SyncAction.Type.MULLIGAN_COMPLETE, whoAmI.name()));
    }

    return mulliganedCard;
  }

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
    if (cards.size() < 10) {
      Log.w(TAG, "CardDecks not setup");
    }

    Random random = new Random();
    // 10 random unique cards from set cardDecks
    HashMap<Integer, Card> drawnCards = new HashMap<>();
    while (drawnCards.size() < 10) {
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

  public void performAction(CardAction action, ActionParams params) {
    if (!gameStateMachine.stateEquals(GameState.START_PLAYER_TURN)) {
      return;
    }

    if (action.performed) {
      Log.w(TAG, "Action is already performed.");
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
        // remove card from hand
        // TODO: Test removing of card
        gameField
            .getCardDeck(gameField.getCurrentPlayer().getInitialPlayerInformation())
            .remove(card.getFirebaseId());

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
        Log.w(TAG, "Action not in performAction implemented");
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

  private void notifyCardActionCallbacks(CardAction action, ActionParams params) {
    for (CardActionCallback callback : cardActionCallbacks) {
      callback.didPerformAction(action, params);
    }
  }

  public void registerGameFieldListener(GameFieldObserver observer) {
    if (gameFieldObservers.contains(observer)) return;
    gameFieldObservers.add(observer);
    observer.updateGameField(gameField);
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
