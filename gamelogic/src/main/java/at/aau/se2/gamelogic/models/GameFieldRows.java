package at.aau.se2.gamelogic.models;

import java.util.HashMap;

import at.aau.se2.gamelogic.GameLogic;

public class GameFieldRows {
  private HashMap<String, Card> p1MeleeRow = new HashMap<>(GameLogic.ROW_CARD_NUMBER);
  private HashMap<String, Card> p1RangeRow = new HashMap<>(GameLogic.ROW_CARD_NUMBER);
  private HashMap<String, Card> p2MeleeRow = new HashMap<>(GameLogic.ROW_CARD_NUMBER);
  private HashMap<String, Card> p2RangeRow = new HashMap<>(GameLogic.ROW_CARD_NUMBER);

  // Still wrong, because we dont know who is me.
  public HashMap<String, Card> meleeRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1MeleeRow;
      case OPPONENT:
        return p2MeleeRow;
      default:
        return null;
    }
  }

  public HashMap<String, Card> rangedRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1RangeRow;
      case OPPONENT:
        return p2RangeRow;
      default:
        return null;
    }
  }

  public boolean setCardIfPossible(InitialPlayer player, RowType rowType, int location, Card card) {
    HashMap<String, Card> row;
    switch (rowType) {
      case MELEE:
        row = meleeRowFor(player);
        break;
      case RANGED:
        row = rangedRowFor(player);
        break;
      default:
        return false;
    }

    if (row.get(location + "_index") != null) return false;

    row.put(location + "_index", card);
    return true;
  }

  public HashMap<String, Card> getP1MeleeRow() {
    return p1MeleeRow;
  }

  public HashMap<String, Card> getP1RangeRow() {
    return p1RangeRow;
  }

  public HashMap<String, Card> getP2MeleeRow() {
    return p2MeleeRow;
  }

  public HashMap<String, Card> getP2RangeRow() {
    return p2RangeRow;
  }
}
