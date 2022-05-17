package at.aau.se2.gwent;

import java.util.ArrayList;
import java.util.Objects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.CardActionCallback;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gwent.databinding.FragmentStartBinding;

public class StartFragment extends Fragment implements CardActionCallback {
  private static final String TAG = StartFragment.class.getSimpleName();

  private FragmentStartBinding binding;
  private GameLogic gameLogic =
      new GameLogic(
          new Player(1, InitialPlayer.INITIATOR),
          new CardDecks(new ArrayList<>(), new ArrayList<>()));

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gameLogic.registerCardActionCallback(this);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentStartBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.cardView.setupWithCard(4, "WeaponSmith", R.drawable.an_craite_amorsmith);
    binding.buttonFirst.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Log.v(TAG, "DidClick StartGame");
          }
        });
    binding.cardView.setOnLongClickListener(
        new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            Navigation.findNavController(
                    Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
                .navigate(R.id.detailed_card);
            return true;
          }
        });

    binding.deployButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            gameLogic.performAction(
                new CardAction(CardAction.ActionType.DEPLOY),
                new DeployParams(0, new Row(1, RowType.MELEE), 0));
          }
        });

    // Set onclicklistener to start the Boardview (Intent?)
    binding.buttonFirst.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            Intent intent = new Intent(getActivity(), BoardFragment.class);
            ((MainActivity) getActivity()).startActivity(intent);
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void didPerformAction(CardAction action, ActionParams params) {
    Log.v(TAG, "Action Performed: " + action.getType().name());
  }
}
