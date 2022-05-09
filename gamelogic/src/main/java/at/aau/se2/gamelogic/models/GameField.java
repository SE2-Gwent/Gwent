package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import androidx.annotation.Keep;

public class GameField {
  private GameFieldRows rows;
  private Player currentPlayer;
  private Player opponent;
  private CardDecks cardDecks;
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

  public GameFieldRows getRows() {
    return rows;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public Player getOpponent() {
    return opponent;
  }

  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  public CardDecks getCardDecks() {
    return cardDecks;
  }

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }
}
