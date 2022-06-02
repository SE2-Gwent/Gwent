package at.aau.se2.gamelogic.comunication;

public interface CommuncationObserver {
  void didSyncChanges(SyncRoot root);
}
