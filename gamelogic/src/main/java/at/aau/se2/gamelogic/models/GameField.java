package at.aau.se2.gamelogic.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.database.Exclude;

import android.util.Log;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;

public class GameField {
  private final String TAG = GameField.class.getSimpleName();

  private GameFieldRows rows = new GameFieldRows();
  @Nullable private Player currentPlayer;
  @Nullable private Player opponent;
  // The two cardDecks both players are playing with
  private CardDecks cardDecks = new CardDecks();
  // Cards the player should get displayed in their hand
  private CardDecks currentHandCards = new CardDecks();
  private ArrayList<Hero> heroes;

  @Keep
  public GameField() {}

  public GameField(
      GameFieldRows rows,
      Player currentPlayer,
      Player opponent,
      CardDecks cardDecks,
      ArrayList<Hero> heroes) {
    this.rows = rows;
    this.currentPlayer = currentPlayer;
    this.opponent = opponent;
    this.cardDecks = cardDecks;
    this.heroes = heroes;
  }

  @Exclude
  public int getRoundNumber() {
    if (currentPlayer == null || opponent == null) {
      Log.i(TAG, "Cannot calculate round. Players are not setup correctly");
      return 0;
    }
    return currentPlayer.getCurrentMatchPoints() + opponent.getCurrentMatchPoints();
  }

  @Exclude
  public @Nullable Player getPointLeadingPlayer() {
    int currentPoints = getPointsForPlayer(currentPlayer);
    int opponentPoints = getPointsForPlayer(opponent);

    if (currentPoints == opponentPoints) {
      return null;
    }

    return (getPointsForPlayer(currentPlayer) > getPointsForPlayer(opponent))
        ? currentPlayer
        : opponent;
  }

  @Exclude
  public int getPointsForPlayer(Player player) {
    if (player == null) return 0;

    int points = 0;

    for (Card card : getRows().meleeRowFor(player.getInitialPlayerInformation())) {
      if (card == null) continue;
      points = +card.getPower() + card.getPowerDiff();
    }
    for (Card card : getRows().rangedRowFor(player.getInitialPlayerInformation())) {
      if (card == null) continue;
      points = +card.getPower() + card.getPowerDiff();
    }

    return points;
  }

  @Exclude
  public Player getWinnerOrNull() {
    if (currentPlayer.getCurrentMatchPoints() == 2) return currentPlayer;
    if (opponent.getCurrentMatchPoints() == 2) return opponent;
    return null;
  }

  public void updateGameDecksAfterMulligan() {
    for (String cardId : currentHandCards.getP1Deck().keySet()) {
      cardDecks.getP1Deck().remove(cardId);
    }

    for (String cardId : currentHandCards.getP2Deck().keySet()) {
      cardDecks.getP2Deck().remove(cardId);
    }
  }

  public void setPlayingCardsFor(InitialPlayer player, ArrayList<Card> cards) {
    currentHandCards.setDeck(player, cards);
  }

  public void setCardDeckFor(InitialPlayer player, ArrayList<Card> cards) {
    cardDecks.setDeck(player, cards);
  }

  public GameFieldRows getRows() {
    return rows;
  }

  @Nullable
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(@Nullable Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  @Nullable
  public Player getOpponent() {
    return opponent;
  }

  public void setOpponent(@Nullable Player opponent) {
    this.opponent = opponent;
  }

  public Player getPlayer(InitialPlayer initialPlayer) {
    return initialPlayer == InitialPlayer.INITIATOR ? currentPlayer : opponent;
  }

  public CardDecks getCardDecks() {
    return cardDecks;
  }

  public HashMap<String, Card> getCardDeck(InitialPlayer initialPlayer) {
    return initialPlayer == InitialPlayer.INITIATOR ? cardDecks.getP1Deck() : cardDecks.getP2Deck();
  }

  public HashMap<String, Card> getCurrentHandCardsFor(InitialPlayer initialPlayer) {
    return initialPlayer == InitialPlayer.INITIATOR
        ? currentHandCards.getP1Deck()
        : currentHandCards.getP2Deck();
  }

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }

  public CardDecks getCurrentHandCards() {
    return currentHandCards;
  }

  public void setCardDecks(CardDecks cardDecks) {
    this.cardDecks = cardDecks;
  }
}
