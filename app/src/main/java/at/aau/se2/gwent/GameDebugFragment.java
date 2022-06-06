package at.aau.se2.gwent;

import java.util.Objects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
          binding.playersTurnTextView.setText(viewState.getPlayersTurn());
          binding.roundTextView.setText(viewState.getRoundNumber());
          binding.cardPointsView.setText(viewState.getCombinedPlayerPoints());
          binding.roundPointsView.setText(viewState.getRoundsWon());
          binding.mulliganCardsLeft.setText("CardsToMulligan: " + viewState.getMulliganCardsLeft());
        });

    viewModel.errorMutableLiveData.observe(
        this,
        error -> {
          Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });

    binding.cancelMulliganCardButton.setOnClickListener(
        view -> {
          viewModel.cancelMulligan();
        });
    binding.mulliganCardButton.setOnClickListener(
        view -> {
          Navigation.findNavController(
                  Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
              .navigate(R.id.action_gameDebugFragment_to_mulliganCardFragment);
        });
    binding.roundDoneButton.setOnClickListener(
        view -> {
          viewModel.roundDone();
        });
    binding.passRoundButton.setOnClickListener(
        view -> {
          viewModel.passRound();
        });
  }
}
