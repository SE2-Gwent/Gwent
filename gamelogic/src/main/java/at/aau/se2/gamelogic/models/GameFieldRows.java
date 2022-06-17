package at.aau.se2.gamelogic.models;

import java.util.HashMap;

import androidx.annotation.Keep;

public class GameFieldRows {
  private Row p1MeleeRow = new Row(0, RowType.MELEE);
  private Row p1RangeRow = new Row(1, RowType.RANGED);
  private Row p2MeleeRow = new Row(1, RowType.MELEE);
  private Row p2RangeRow = new Row(3, RowType.RANGED);

  @Keep
  public GameFieldRows() {}

  // Still wrong, because we dont know who is me.
  public HashMap<String, Card> meleeRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1MeleeRow.getPlayerRow();
      case OPPONENT:
        return p2MeleeRow.getPlayerRow();
      default:
        return null;
    }
  }

  public HashMap<String, Card> rangedRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1RangeRow.getPlayerRow();
      case OPPONENT:
        return p2RangeRow.getPlayerRow();
      default:
        return null;
    }
  }

  public Row getMeleeRowFor(InitialPlayer player) {
    switch (player) {
      case INITIATOR:
        return p1MeleeRow;
      case OPPONENT:
        return p2MeleeRow;
      default:
        return null;
    }
  }

  public Row getRangedRowFor(InitialPlayer player) {
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

  /*
  IMPORTANT NOTE: Pay close attention when calling the getter methods! Normally the getter functions,
  which return the HashMap don't belong here (they should be within the Row Class)!
   */
  public HashMap<String, Card> getMeleeRowForP1() {
    return p1MeleeRow.getPlayerRow();
  }

  public HashMap<String, Card> getRangeRowForP1() {
    return p1RangeRow.getPlayerRow();
  }

  public HashMap<String, Card> getMeleeRowForP2() {
    return p2MeleeRow.getPlayerRow();
  }

  public HashMap<String, Card> getRangeRowForP2() {
    return p2RangeRow.getPlayerRow();
  }

  public Row getP1MeleeRow() {
    return p1MeleeRow;
  }

  public Row getP1RangeRow() {
    return p1RangeRow;
  }

  public Row getP2MeleeRow() {
    return p2MeleeRow;
  }

  public Row getP2RangeRow() {
    return p2RangeRow;
  }
}
