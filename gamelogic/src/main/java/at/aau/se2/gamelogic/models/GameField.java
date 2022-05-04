package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

public class GameField {
  private GameFieldRows rows;
  private Player currentPlayer;
  private Player opponent;
  private CardDecks cardDecks;
  private ArrayList<Hero> heroes;

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

  public void setRows(GameFieldRows rows) {
    this.rows = rows;
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

  public void setCardDecks(CardDecks cardDecks) {
    this.cardDecks = cardDecks;
  }

  public ArrayList<Hero> getHeroes() {
    return heroes;
  }

  public void setHeroes(ArrayList<Hero> heroes) {
    this.heroes = heroes;
  }
}
