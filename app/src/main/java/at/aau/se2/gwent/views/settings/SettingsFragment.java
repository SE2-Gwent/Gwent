package at.aau.se2.gwent.views.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
  private SettingsViewModel viewModel;
  private FragmentSettingsBinding binding;
  private AppBarConfiguration appBarConfiguration;

  public static SettingsFragment newInstance() {
    return new SettingsFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    viewModel.getCurrentState().observe(this, this::updateUI);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSettingsBinding.inflate(inflater, container, false);

    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.rulesToolBar);
    NavController navController =
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
    appBarConfiguration =
        new AppBarConfiguration.Builder(R.id.nav_host_fragment_content_main).build();
    NavigationUI.setupActionBarWithNavController(
        (AppCompatActivity) getActivity(), navController, appBarConfiguration);

    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  private void updateUI(SettingsViewModel.ViewState state) {
    switch (state) {
      case INITIAL:
        break; // could show loading or so
      case LOADED:
        binding.versionNameTextView.setText(viewModel.getVersionName());
        binding.versionCodeTextView.setText(viewModel.getVersionCode());
    }
  }
}
