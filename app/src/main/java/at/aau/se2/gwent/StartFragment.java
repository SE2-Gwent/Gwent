package at.aau.se2.gwent;

import java.util.ArrayList;
import java.util.Objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import at.aau.se2.gamelogic.CardAction;
import at.aau.se2.gamelogic.CardActionCallback;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.comunication.Result;
import at.aau.se2.gamelogic.comunication.ResultObserver;
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
  private GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

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

    binding.startGameButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            gameLogic.startGame(
                result -> {
                  switch (result.getType()) {
                    case SUCCESS: // TODO: go to gameboard
                      Toast.makeText(
                              getContext(),
                              "Game Created: " + result.getValue(),
                              Toast.LENGTH_SHORT)
                          .show();

                      initializeGameField();

                      NavController controller =
                          Navigation.findNavController(
                              Objects.requireNonNull(getActivity()),
                              R.id.nav_host_fragment_content_main);
                      controller.navigate(R.id.action_StartFragment_to_gameDebugFragment);

                      break;
                    case FAILURE:
                      Toast.makeText(getContext(), "Could not create game", Toast.LENGTH_SHORT)
                          .show();
                      break;
                  }
                });
          }
        });

    binding.joinGameButton.setOnClickListener(
        button -> {
          showJoinDialog(getContext());
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
  }

  private void initializeGameField() {
    gameLogic.initializeGame(
        new Player(1, InitialPlayer.INITIATOR),
        new CardDecks(new ArrayList<>(), new ArrayList<>()),
        new ArrayList<>());
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

  @Override
  public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    super.onMultiWindowModeChanged(isInMultiWindowMode);
  }

  private void showJoinDialog(Context context) {
    final EditText taskEditText = new EditText(context);
    AlertDialog dialog =
        new AlertDialog.Builder(context)
            .setTitle("Join Game")
            .setMessage("Input GameId from other Player.")
            .setView(taskEditText)
            .setPositiveButton(
                "Join",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    String gameId = String.valueOf(taskEditText.getText());
                    gameLogic.joinGame(
                        Integer.parseInt(gameId),
                        new ResultObserver<Integer, Error>() {
                          @Override
                          public void finished(Result<Integer, Error> result) {
                            NavController controller =
                                Navigation.findNavController(
                                    Objects.requireNonNull(getActivity()),
                                    R.id.nav_host_fragment_content_main);
                            controller.navigate(R.id.action_StartFragment_to_gameDebugFragment);
                          }
                        });
                  }
                })
            .setNegativeButton("Cancel", null)
            .create();
    dialog.show();
  }
}
