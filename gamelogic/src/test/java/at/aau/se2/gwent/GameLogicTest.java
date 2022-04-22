package at.aau.se2.gwent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;

public class GameLogicTest {
  GameLogic sut = new GameLogic();

  @Test
  public void testPerformActionMappings() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new DeployParams(1, Row.MELEE));

    assertTrue(action.isPerformed());
  }

  @Test
  public void testPerformActionWithWrongParams() {
    CardAction action = new CardAction(CardAction.ActionType.DEPLOY);

    sut.performAction(action, new AttackParams());

    assertFalse(action.isPerformed());
  }

}
