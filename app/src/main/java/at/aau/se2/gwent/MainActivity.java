package at.aau.se2.gwent;

import java.util.ArrayList;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import at.aau.se2.gamelogic.GameLogicDataProvider;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.databinding.ActivityMainBinding;
import at.aau.se2.gwent.util.DeckGeneration;

public class MainActivity extends AppCompatActivity implements GameLogicDataProvider {
  private static final String TAG = MainActivity.class.getSimpleName();

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    Environment.getSharedInstance().getGameLogic().setGameLogicDataProvider(this);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return navController.navigateUp();
  }

  @Override
  public ArrayList<Card> needsCardDeck() {
    return DeckGeneration.generateCardDeck(getApplicationContext());
  }
}
