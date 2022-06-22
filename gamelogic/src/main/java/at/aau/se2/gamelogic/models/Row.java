package at.aau.se2.gamelogic.models;

import java.util.HashMap;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.GameLogic;

public class Row {
  private int id;
  private HashMap<String, Card> playerRow = new HashMap<>(GameLogic.ROW_CARD_NUMBER);
  private RowType rowType;
  private RowStatus rowStatus;
  private int remainingStatusRounds;

  public Row(int id, RowType rowType) {
    this.id = id;
    this.rowType = rowType;
  }

  @Keep
  public Row() {}

  public HashMap<String, Card> getPlayerRow() {
    return playerRow;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public RowType getRowType() {
    return rowType;
  }

  public void setRowType(RowType rowType) {
    this.rowType = rowType;
  }

  public RowStatus getRowStatus() {
    return rowStatus;
  }

  public void setRowStatus(RowStatus rowStatus) {
    this.rowStatus = rowStatus;
  }

  public int getRemainingStatusRounds() {
    return remainingStatusRounds;
  }

  public void setRemainingStatusRounds(int remainingStatusRounds) {
    this.remainingStatusRounds = remainingStatusRounds;
  }
}
