package at.aau.se2.communication;

import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import at.aau.se2.communication.models.SyncRoot;

public class FirebaseConnector {
  private static final int MIN_ID = 1000;
  private static final int MAX_ID = 8000;
  private DatabaseReference databaseRef;
  private DatabaseReference gameRef = null;

  ValueEventListener postListener =
      new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          SyncRoot update = dataSnapshot.getValue(SyncRoot.class);
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

  private void createNewGameNode(int id, ResultObserver<Integer, Error> observer) {
    String childId = String.valueOf(id);

    SyncRoot syncRoot = new SyncRoot();

    gameRef = databaseRef.child(childId).getRef();
    databaseRef.child(childId).setValue(syncRoot);
    databaseRef.child(childId).addValueEventListener(postListener);

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
