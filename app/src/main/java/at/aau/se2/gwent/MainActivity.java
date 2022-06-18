package at.aau.se2.gwent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import at.aau.se2.gwent.databinding.ActivityMainBinding;
import at.aau.se2.gwent.views.common.FragmentBackPressListener;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  private AppBarConfiguration appBarConfiguration;
  private ActivityMainBinding binding;

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
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            .navigate(R.id.settings);
        return true;
      case R.id.action_detailed_card:
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            .navigate(R.id.detailed_card);
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
