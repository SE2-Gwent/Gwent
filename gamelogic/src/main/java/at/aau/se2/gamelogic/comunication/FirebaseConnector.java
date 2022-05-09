package at.aau.se2.gamelogic.comunication;

import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import androidx.annotation.NonNull;
import at.aau.se2.gamelogic.models.GameField;

public class FirebaseConnector {
  private static final String TAG = FirebaseConnector.class.getSimpleName();
  private static final int MIN_ID = 1000;
  private static final int MAX_ID = 8000;
  private DatabaseReference databaseRef;
  private DatabaseReference gameRef = null;

  ValueEventListener postListener =
      new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          SyncRoot update = dataSnapshot.getValue(SyncRoot.class);
          Log.v(TAG, "Synced");
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

  public void joinGame(int id, ResultObserver<Integer, Error> observer) {
    gameExists(
        id,
        result -> {
          switch (result.getType()) {
            case SUCCESS:
              gameRef = databaseRef.child(String.valueOf(id)).getRef();
              gameRef.addValueEventListener(postListener);
              observer.finished(Result.Success(id));
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

  private void setupDatabaseReference() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    databaseRef = database.getReference("games");
  }

  private void gameExists(int id, ResultObserver<Boolean, DatabaseError> observer) {
    Query test = databaseRef.orderByValue().equalTo(id);

    test.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            observer.finished(Result.Success(snapshot.exists()));
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
            observer.finished(Result.Failure(error));
          }
        });
  }
}
