package at.aau.se2.gamelogic.communication;

// import com.google.firebase.FirebaseApp;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnector {
  public FirebaseConnector() {
    /*        FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                    .build();
    */
    // FirebaseApp.initializeApp();
  }


  private void setupDatabaseReference() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    databaseRef = database.getReference("messages");
    database.getReference("messages").addValueEventListener(postListener);
  }
}
