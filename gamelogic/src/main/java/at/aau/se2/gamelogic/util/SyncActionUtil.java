package at.aau.se2.gamelogic.util;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import at.aau.se2.gamelogic.comunication.SyncAction;
import at.aau.se2.gamelogic.models.InitialPlayer;

public class SyncActionUtil {
  public static @Nullable InitialPlayer findStartingPlayer(ArrayList<SyncAction> actions) {
    for (SyncAction action : actions) {
      if (action.getType() == SyncAction.Type.STARTING_PLAYER) {
        return InitialPlayer.valueOf(action.getMessage());
      }
    }

    return null;
  }
}
