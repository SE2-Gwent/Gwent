package at.aau.se2.gwent;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import at.aau.se2.gwent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = MainActivity.class.getSimpleName();

  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;
  private DatabaseReference databaseRef;
  private ArrayList<String> messages = new ArrayList<>();

  ValueEventListener postListener =
      new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          // Get Post object and use the values to update the UI
          GenericTypeIndicator<ArrayList<String>> t =
              new GenericTypeIndicator<ArrayList<String>>() {};
          messages = dataSnapshot.getValue(t);
          if (messages == null) {
            messages = new ArrayList<>();
          }
          Log.v(TAG, "Messages: Max " + messages);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
      };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.toolbar);

    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration =
        new AppBarConfiguration.Builder(R.id.nav_host_fragment_content_main).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    binding.fab.setOnClickListener(
        view -> {
          messages.add("Hello, World!");
          databaseRef.setValue(messages);
        });

    setupDatabaseReference();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  private void setupDatabaseReference() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    databaseRef = database.getReference("messages");
    database.getReference("messages").addValueEventListener(postListener);
    databaseRef.setValue(messages);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            .navigate(R.id.settings);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return navController.navigateUp();
  }

  @Override
  public void onClick(View v) {}
}
