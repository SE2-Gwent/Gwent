package at.aau.se2.gwent.views.rules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentRulesBinding;

public class RulesFragment extends Fragment {
  private FragmentRulesBinding binding;
  private AppBarConfiguration appBarConfiguration;

  public static RulesFragment newInstance() {
    return new RulesFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentRulesBinding.inflate(inflater, container, false);

    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.rulesToolBar);
    NavController navController =
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
    appBarConfiguration =
        new AppBarConfiguration.Builder(R.id.nav_host_fragment_content_main).build();
    NavigationUI.setupActionBarWithNavController(
        (AppCompatActivity) getActivity(), navController, appBarConfiguration);

    // remove longClick to have not selectable text in webview
    binding.fullRules.setLongClickable(false);
    binding.fullRules.setOnLongClickListener(v -> true);
    binding.fullRules.loadUrl("file:///android_asset/rules.html");

    return binding.getRoot();
  }
}
