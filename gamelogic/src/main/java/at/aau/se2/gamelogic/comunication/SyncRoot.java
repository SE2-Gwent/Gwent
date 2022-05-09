package at.aau.se2.gamelogic.comunication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.firebase.database.Exclude;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.GameField;

public class SyncRoot {
  private String createdOn = new Date().toString();
  private HashMap<String, SyncAction> syncActions = new HashMap<>(); // firebase needs String keys
  private GameField gameField;

  // not synced
  @Exclude private int lastSyncActionSize = 0;

  @Keep
  public SyncRoot() {}

  public void addAction(SyncAction action) {
    int newKey = syncActions.size();
    // need to add chars to satisfy firebase
    // https://stackoverflow.com/questions/44032887/firebase-database-error-expected-a-map-while-deserializing-but-got-a-class-ja
    syncActions.put(newKey + "_key", action);
  }

  public ArrayList<SyncAction> getLastActions() {
    ArrayList<SyncAction> lastActions = new ArrayList<>();

    for (int i = lastSyncActionSize; i < syncActions.size(); i++) {
      lastActions.add(syncActions.get(i + "_key"));
    }

    lastSyncActionSize = syncActions.size();
    return lastActions;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public GameField getGameField() {
    return gameField;
  }

  public HashMap<String, SyncAction> getSyncActions() {
    return syncActions;
  }
}
