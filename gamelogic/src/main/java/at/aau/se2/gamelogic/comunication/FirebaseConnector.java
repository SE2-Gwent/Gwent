package at.aau.se2.gamelogic.comunication;

import java.util.ArrayList;
import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import at.aau.se2.gamelogic.models.GameField;

public class FirebaseConnector {
  private static final String TAG = FirebaseConnector.class.getSimpleName();
  private static final int MIN_ID = 1000;
  private static final int MAX_ID = 8000;
  private DatabaseReference databaseRef;
  private DatabaseReference gameRef = null;
  private final ArrayList<CommuncationObserver> communicationObservers = new ArrayList<>();
  private SyncRoot syncRoot;

  ValueEventListener postListener =
      new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          SyncRoot update = dataSnapshot.getValue(SyncRoot.class);
          syncRoot = update;
          for (CommuncationObserver observer : communicationObservers) {
            observer.didSyncChanges(update);
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
      };

  public FirebaseConnector() {
    setupDatabaseReference();
  }

  public void createGame(ResultObserver<Integer, Error> observer) {
    Random random = new Random();
    int number = MIN_ID + random.nextInt(MAX_ID);

    gameExists(
        number,
        result -> {
          switch (result.getType()) {
            case SUCCESS:
              createNewGameNode(number, observer);
              break;

            case FAILURE:
              // just try again if game already exists
              createGame(observer);
              break;
          }
        });
  }

  public void joinGame(int id, ResultObserver<GameField, Error> observer) {
    gameExists(
        id,
        result -> {
          switch (result.getType()) {
            case SUCCESS:
              gameRef = databaseRef.child(String.valueOf(id)).getRef();
              gameRef.addValueEventListener(postListener);
              gameRef.addListenerForSingleValueEvent(
                  new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      SyncRoot update = snapshot.getValue(SyncRoot.class);
                      observer.finished(Result.Success(update.getGameField()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                  });
              break;

            case FAILURE:
              observer.finished(Result.Failure(ConnectorError.GameNotFound()));
              break;
          }
        });
  }

  public void syncGameField(GameField gameField) {
    gameRef.child("gameField").setValue(gameField);
  }

  private void createNewGameNode(int id, ResultObserver<Integer, Error> observer) {
    String childId = String.valueOf(id);

    SyncRoot syncRoot = new SyncRoot();

    gameRef = databaseRef.child(childId).getRef();
    gameRef.setValue(syncRoot, null);
    gameRef.addValueEventListener(postListener);

    observer.finished(Result.Success(id));
  }

  public void sendSyncAction(SyncAction action) {
    syncRoot.addAction(action);
    gameRef.setValue(syncRoot);
  }

  private void setupDatabaseReference() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    databaseRef = database.getReference("games");
  }

  private void gameExists(int id, ResultObserver<Boolean, Error> observer) {
    Query test = databaseRef.orderByValue().equalTo(id);

    test.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null) {
              observer.finished(Result.Failure(ConnectorError.GameNotFound()));
              return;
            }

            observer.finished(Result.Success(snapshot.exists()));
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            observer.finished(Result.Failure(ConnectorError.GameNotFound()));
          }
        });
  }

  public void addListener(CommuncationObserver listener) {
    if (communicationObservers.contains(listener)) return;
    communicationObservers.add(listener);
  }
}
