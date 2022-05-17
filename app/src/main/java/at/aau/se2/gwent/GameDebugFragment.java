package at.aau.se2.gwent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gwent.databinding.GameDebugFragmentBinding;

public class GameDebugFragment extends Fragment {
  private GameDebugViewModel viewModel;
  private GameDebugFragmentBinding binding;

  public static GameDebugFragment newInstance() {
    return new GameDebugFragment();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = GameDebugFragmentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(GameDebugViewModel.class);
    viewModel.state.observe(
        this,
        viewState -> {
          binding.gameIdTextView.setText(viewState.getGameId());
          binding.stateTextView.setText(viewState.getState());
          binding.playerTextView.setText(viewState.getPlayer());
          binding.startingPlayerTextView.setText(viewState.getStartingPlayer());
        });

    binding.cancelMulliganCardButton.setOnClickListener(
        view -> {
          viewModel.cancelMulligan();
        });
    binding.mulliganCardButton.setOnClickListener(
        view -> {
          viewModel.mulliganCard();
        });
  }
}
