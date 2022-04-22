package at.aau.se2.gamelogic.models.cardactions;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.Player;

public class GameFieldRows {
  private ArrayList<Card> p1MeleeRow = new ArrayList<>();
  private ArrayList<Card> p1RangeRow = new ArrayList<>();
  private ArrayList<Card> p2MeleeRow = new ArrayList<>();
  private ArrayList<Card> p2RangeRow = new ArrayList<>();

  // Still wrong, because we dont know who is me.
  public ArrayList<Card> meleeRowFor(Player player) {
    switch (player) {
      case ME:
        return p1MeleeRow;
      case OPPONENT:
        return p2MeleeRow;
      default:
        return null;
    }
  }

  public ArrayList<Card> rangedRowFor(Player player) {
    switch (player) {
      case ME:
        return p1RangeRow;
      case OPPONENT:
        return p2RangeRow;
      default:
        return null;
    }
  }
}
