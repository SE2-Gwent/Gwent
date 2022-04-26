package at.aau.se2.gamelogic;

public class CardAction {
  public enum ActionType {
    DEPLOY(1),
    ATTACK(2),
    FOG(3);

    private final int id;

    ActionType(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }
  }

  boolean performed;

  private final ActionType type;

  public CardAction(ActionType actionType) {
    this.type = actionType;
  }

  public ActionType getType() {
    return type;
  }

  public boolean isPerformed() {
    return performed;
  }
}
