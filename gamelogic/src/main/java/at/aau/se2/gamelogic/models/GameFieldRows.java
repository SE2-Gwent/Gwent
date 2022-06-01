package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import androidx.annotation.Keep;

public class GameFieldRows {
  private ArrayList<Card> p1MeleeRow = new ArrayList<>();
  private ArrayList<Card> p1RangeRow = new ArrayList<>();
  private ArrayList<Card> p2MeleeRow = new ArrayList<>();
  private ArrayList<Card> p2RangeRow = new ArrayList<>();

  @Keep
  public GameFieldRows() {}

  // Still wrong, because we dont know who is me.
  public ArrayList<Card> meleeRowFor(Player player) {
    switch (player.getInitialPlayerInformation()) {
      case INITIATOR:
        return p1MeleeRow;
      case OPPONENT:
        return p2MeleeRow;
      default:
        return null;
    }
  }

  public ArrayList<Card> rangedRowFor(Player player) {
    switch (player.getInitialPlayerInformation()) {
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
