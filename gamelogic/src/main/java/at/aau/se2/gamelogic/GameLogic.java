package at.aau.se2.gamelogic;

import java.util.ArrayList;

import android.util.Log;
import androidx.annotation.Nullable;
import at.aau.se2.gamelogic.comunication.FirebaseConnector;
import at.aau.se2.gamelogic.comunication.Result;
import at.aau.se2.gamelogic.comunication.ResultObserver;
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

public class GameLogic {
  private static final String TAG = GameLogic.class.getSimpleName();
  private int gameId = -1;
  private GameField gameField;
  private InitialPlayer whoAmI;
  private FirebaseConnector connector;
  private ArrayList<CardActionCallback> cardActionCallbacks = new ArrayList<>();
  private GameStateMachine gameStateMachine = new GameStateMachine();

  public GameLogic(
      @Nullable FirebaseConnector connector, @Nullable GameStateMachine gameStateMachine) {
    this.connector = (connector != null) ? connector : new FirebaseConnector();
    this.gameStateMachine = (gameStateMachine != null) ? gameStateMachine : new GameStateMachine();
  }

  public void registerCardActionCallback(CardActionCallback callback) {
    if (cardActionCallbacks.contains(callback)) {
      return;
    }

    cardActionCallbacks.add(callback);
  }

  // GameState Manipulation

  public void startGame(ResultObserver<Integer, Error> observer) {
    if (!gameStateMachine.canProgressTo(GameState.START_GAME_ROUND)) {
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
            }

            observer.finished(result);
          }
        });
  }
    gameField =
        new GameField(
            gameFieldRows, currentPlayer, new Player(2, InitialPlayer.OPPONENT), cardDecks, heroes);
  }

  // Card Actions

  public void performAction(CardAction action, ActionParams params) {
    if (!gameStateMachine.stateEquals(GameState.START_GAME_ROUND)) {
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

  // Getters & Setters

  public int getGameId() {
    return gameId;
  }

  public GameState getCurrentGameState() {
    return gameStateMachine.getCurrent();
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
}
