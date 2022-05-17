package at.aau.se2.gamelogic;

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

// TODO: Mulligan 3 / 1 Cards
// TODO: End Round / Let player pass
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
  private int cardMulligansLeft = 0;

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

  public void mulliganCard(int cardId) {
    if (!gameStateMachine.stateEquals(GameState.MULLIGAN_CARDS)) return;
    if (cardMulligansLeft == 0) {
      Log.w(TAG, "There are no mulligans left this round");
      return;
    }

    ArrayList<Card> cards = new ArrayList<Card>(gameField.getCardDeck(whoAmI).values());
    HashMap<Integer, Card> playingCards = gameField.getPlayingCards(whoAmI);

    playingCards.remove(cardId);

    Random random = new Random();
    while (playingCards.size() < 10) {
      int randomIndex = random.nextInt(cards.size());
      Card card = cards.get(randomIndex);
      playingCards.put(card.getId(), card);
    }

    gameField.setPlayingCardsFor(whoAmI, new ArrayList<>(playingCards.values()));
    connector.syncGameField(gameField);
    cardMulligansLeft -= 1;
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

    Random random = new Random();
    // 10 random unique cards from set cardDecks
    HashMap<Integer, Card> drawnCards = new HashMap<>();
    while (drawnCards.size() < 10) {
      int randomIndex = random.nextInt(cards.size());
      Card card = cards.get(randomIndex);
      drawnCards.put(card.getId(), card);
    }

    gameField.setPlayingCardsFor(whoAmI, new ArrayList<>(drawnCards.values()));

    if (gameStateMachine.cardsDrawn()) {
      connector.syncGameField(gameField);
    }
  }

  protected void handleGameSyncUpdates(SyncRoot syncRoot) {
    if (syncRoot == null) return;

    this.gameField = syncRoot.getGameField();

    ArrayList<SyncAction> newSyncActions = syncRoot.getLastActions();

    switch (gameStateMachine.getCurrent()) {
      case WAIT_FOR_OPPONENT:
        if (syncRoot.getGameField() != null && syncRoot.getGameField().getOpponent() != null) {
          // Somebody joined the game
          gameStateMachine.opponentJoined();
          firstGameSetup();
        }
        break;

      case START_GAME_ROUND:
        InitialPlayer startingPlayer = SyncActionUtil.findStartingPlayer(newSyncActions);
        if (startingPlayer == null || gameField == null) return;

        cardMulligansLeft = (gameField.getRoundNumber() == 0) ? 3 : 1;

        if (gameStateMachine.roundCanStart()) {
          this.startingPlayer = startingPlayer;
        }
        break;

      case DRAW_CARDS:
        if (gameField == null || gameField.getCurrentPlayer() == null) return;
        drawCards();

      case MULLIGAN_CARDS:
        if (cardMulligansLeft > 0) return;
        gameStateMachine.cardsChanged();

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
    ArrayList<Card> playingCards = new ArrayList<>(gameField.getPlayingCards(whoAmI).values());
    for (int i = 0; i < 6; i++) {
      cardsToMulligan.add(playingCards.get(i));
    }
    return cardsToMulligan;
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

  protected void setWhoAmI(InitialPlayer player) {
    this.whoAmI = player;
  }

  public int getCardMulligansLeft() {
    return cardMulligansLeft;
  }

  protected void setCardMulligansLeft(int left) {
    this.cardMulligansLeft = left;
  }
}
