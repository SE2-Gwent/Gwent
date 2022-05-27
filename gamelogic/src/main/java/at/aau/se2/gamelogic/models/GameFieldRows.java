package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.GameLogic;

public class GameFieldRows {
  private ArrayList<Card> p1MeleeRow = new ArrayList<>();
  private ArrayList<Card> p1RangeRow = new ArrayList<>();
  private ArrayList<Card> p2MeleeRow = new ArrayList<>();
  private ArrayList<Card> p2RangeRow = new ArrayList<>();

  @Keep
  public GameFieldRows() {
    for (ArrayList<Card> row : new ArrayList[] {p1MeleeRow, p1RangeRow, p2MeleeRow, p2RangeRow}) {
      for (int i = 0; i < GameLogic.ROW_CARD_NUMBER; i++) {
        row.add(null);
      }
    }
  }

  // Still wrong, because we dont know who is me.
  public ArrayList<Card> meleeRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1MeleeRow;
      case OPPONENT:
        return p2MeleeRow;
      default:
        return null;
    }
  }

  public ArrayList<Card> rangedRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1RangeRow;
      case OPPONENT:
        return p2RangeRow;
      default:
        return null;
    }
  }

  public ArrayList<Card> getP1MeleeRow() {
    return p1MeleeRow;
  }

  public ArrayList<Card> getP1RangeRow() {
    return p1RangeRow;
  }

  public ArrayList<Card> getP2MeleeRow() {
    return p2MeleeRow;
  }

  public ArrayList<Card> getP2RangeRow() {
    return p2RangeRow;
  }
}
