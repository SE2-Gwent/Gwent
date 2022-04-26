package at.aau.se2.gamelogic;

import at.aau.se2.gamelogic.models.cardactions.ActionParams;

public interface CardActionCallback {
  void didPerformAction(CardAction action, ActionParams params);
}
