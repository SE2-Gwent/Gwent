package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

public class GameField {
  private GameFieldRows rows;
  @Nullable private Player currentPlayer;
  @Nullable private Player opponent;
  private CardDecks cardDecks;
  private CardDecks playingCards = new CardDecks();
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

  public void setPlayingCardsFor(InitialPlayer player, ArrayList<Card> cards) {
    playingCards.setDeck(player, cards);
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

  public CardDecks getCardDecks() {
    return cardDecks;
  }

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }

  public CardDecks getPlayingCards() {
    return playingCards;
  }

  public void setCardDecks(CardDecks cardDecks) {
    this.cardDecks = cardDecks;
  }
}
