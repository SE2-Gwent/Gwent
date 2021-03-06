package at.aau.se2.gwent.views.startMenu;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
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
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gwent.Environment;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
  private static final String TAG = StartFragment.class.getSimpleName();

  private FragmentStartBinding binding;
  private GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
                    case SUCCESS:
                      Toast.makeText(
                              getContext(),
                              String.format(
                                  "%s %d", getString(R.string.game_created), result.getValue()),
                              Toast.LENGTH_SHORT)
                          .show();

                      NavController controller =
                          Navigation.findNavController(
                              requireActivity(), R.id.nav_host_fragment_content_main);
                      controller.navigate(R.id.action_StartFragment_to_board_fragment);

                      break;
                    case FAILURE:
                      Toast.makeText(
                              getContext(), R.string.could_not_create_game, Toast.LENGTH_SHORT)
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

    binding.actionSettings.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.settings);
          }
        });

    binding.rulesButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.rules);
          }
        });
  }

  @Override
  public void onResume() {
    super.onResume();
    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    super.onMultiWindowModeChanged(isInMultiWindowMode);
  }

  private void showJoinDialog(Context context) {
    final EditText taskEditText = new EditText(context);
    taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
    new MaterialAlertDialogBuilder(context)
        .setTitle(R.string.join_game)
        .setMessage(R.string.join_game_instructions)
        .setView(taskEditText)
        .setPositiveButton(
            R.string.join,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                String gameId = String.valueOf(taskEditText.getText());
                gameLogic.joinGame(
                    Integer.parseInt(gameId),
                    result -> {
                      switch (result.getType()) {
                        case SUCCESS:
                          NavController controller =
                              Navigation.findNavController(
                                  requireActivity(), R.id.nav_host_fragment_content_main);
                          controller.navigate(R.id.action_StartFragment_to_board_fragment);
                          break;
                        case FAILURE:
                          Toast.makeText(
                                  getContext(), R.string.could_not_join_game, Toast.LENGTH_SHORT)
                              .show();
                          break;
                      }
                    });
              }
            })
        .setNegativeButton(R.string.cancel, null)
        .show();
  }
}
