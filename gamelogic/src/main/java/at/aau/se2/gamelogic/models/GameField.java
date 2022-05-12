package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

public class GameField {
  private final GameFieldRows rows;
  private Player currentPlayer;
  private Player opponent;
  private final CardDecks cardDecks;
  private final ArrayList<Hero> heroes;

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
