package at.aau.se2.gamelogic.models;

public class Row {
  private int id;
  private RowType rowType;
  private RowStatus rowStatus;
  private int remainingStatusRounds;

  public Row(int id, RowType rowType) {
    this.id = id;
    this.rowType = rowType;
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
