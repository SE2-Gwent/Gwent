package at.aau.se2.gwent.views.debug;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import at.aau.se2.gwent.databinding.FragmentGameDebugBinding;

public class GameDebugFragment extends Fragment {
  private GameDebugViewModel viewModel;
  private FragmentGameDebugBinding binding;
  private AppBarConfiguration appBarConfiguration;

  public static GameDebugFragment newInstance() {
    return new GameDebugFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
        });

    viewModel.errorMutableLiveData.observe(
        this,
        error -> {
          Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGameDebugBinding.inflate(inflater, container, false);

    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.debugToolBar);
    NavController navController =
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
    appBarConfiguration =
        new AppBarConfiguration.Builder(R.id.nav_host_fragment_content_main).build();
    NavigationUI.setupActionBarWithNavController(
        (AppCompatActivity) getActivity(), navController, appBarConfiguration);

    binding.cancelMulliganCardButton.setOnClickListener(
        view -> {
          viewModel.cancelMulligan();
        });
    binding.mulliganCardButton.setOnClickListener(
        view -> {
          viewModel.mulliganCard();
        });
    binding.roundDoneButton.setOnClickListener(
        view -> {
          viewModel.roundDone();
        });
    binding.passRoundButton.setOnClickListener(
        view -> {
          viewModel.passRound();
        });

    return binding.getRoot();
  }
}
