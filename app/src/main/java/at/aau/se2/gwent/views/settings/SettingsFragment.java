package at.aau.se2.gwent.views.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gwent.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {
  private SettingsViewModel viewModel;
  private SettingsFragmentBinding binding;

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
    binding = SettingsFragmentBinding.inflate(inflater, container, false);
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
