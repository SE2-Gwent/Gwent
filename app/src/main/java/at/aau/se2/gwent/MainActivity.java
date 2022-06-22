package at.aau.se2.gwent;

import java.util.ArrayList;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import at.aau.se2.gamelogic.GameLogicDataProvider;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.databinding.ActivityMainBinding;
import at.aau.se2.gwent.util.DeckGeneration;
import at.aau.se2.gwent.views.common.FragmentBackPressListener;

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

  public void onBackPressed() {
    super.onBackPressed();
    Fragment fragment = getCurrentVisibleFragment();
    if (!(fragment instanceof FragmentBackPressListener)) return;
    ((FragmentBackPressListener) fragment).onBackPressed();
  }

  private Fragment getCurrentVisibleFragment() {
    NavHostFragment navHostFragment =
        (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
    FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
    Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
    if (fragment instanceof Fragment) {
      return fragment;
    }
    return null;
  }
}
