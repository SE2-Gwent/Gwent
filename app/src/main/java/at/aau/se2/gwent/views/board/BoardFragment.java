package at.aau.se2.gwent.views.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gamelogic.comunication.SingleEvent;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.databinding.FragmentBoardviewBinding;
import at.aau.se2.gwent.util.CardRowHelper;
import at.aau.se2.gwent.views.common.CardView;

// TODO: Hide NavBar

public class BoardFragment extends Fragment {
  private static final String TAG = BoardFragment.class.getSimpleName();

  private BoardViewModel viewModel;
  private FragmentBoardviewBinding binding;
  private ArrayList<CardView> playersHandCardViews = new ArrayList<>();
  private ArrayList<CardView> opponentsHandCardViews = new ArrayList<>();
  private HashMap<RowType, ArrayList<CardView>> opponentRowCardViews = new HashMap<>();
  private HashMap<RowType, ArrayList<CardView>> playerRowCardViews = new HashMap<>();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
    viewModel.getCurrentState().observe(this, this::updateUI);
    viewModel.getActionLiveData().observe(this, this::handleEvents);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = FragmentBoardviewBinding.inflate(inflater, container, false);
    setupGameRows();
    setupButtons();
    Objects.requireNonNull(getActivity())
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    return binding.getRoot();
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  private void setupGameRows() {
    playerRowCardViews.put(
        RowType.MELEE, CardRowHelper.getCardsFromLayout(binding.playersMeleeRowLayout));
    playerRowCardViews.put(
        RowType.RANGED, CardRowHelper.getCardsFromLayout(binding.playersRangeRowLayout));
    opponentRowCardViews.put(
        RowType.MELEE, CardRowHelper.getCardsFromLayout(binding.opponentsMeleeRowLayout));
    opponentRowCardViews.put(
        RowType.RANGED, CardRowHelper.getCardsFromLayout(binding.opponentsRangeRowLayout));
    CardRowHelper.setBackgroundDrawable(
        new CardareaBinding[] {
          binding.playersMeleeRowLayout,
          binding.playersRangeRowLayout,
          binding.opponentsMeleeRowLayout,
          binding.opponentsRangeRowLayout
        },
        R.drawable.game_board_row_background);

    for (ArrayList<CardView> cardViews : playerRowCardViews.values()) {
      CardRowHelper.setCardsVisibility(cardViews, View.INVISIBLE);
    }
    for (ArrayList<CardView> cardViews : opponentRowCardViews.values()) {
      CardRowHelper.setCardsVisibility(cardViews, View.INVISIBLE);
    }

    CardRowHelper.removeCardViews(binding.playersHandLayout);
    CardRowHelper.removeCardViews(binding.opponentsHandLayout);
  }

  private void setupButtons() {
    binding.primaryRoundButton.setOnClickListener(
        view -> {
          viewModel.didClickPrimaryButton();
        });
  }

  private void updateUI(BoardViewData viewData) {
    binding.primaryRoundButton.setText(viewData.getPrimaryButtonMode().getText());
    binding.primaryRoundButton.setEnabled(viewData.isPrimaryButtonEnabled());

    CardRowHelper.removeCardViews(binding.playersHandLayout);
    playersHandCardViews.clear();
    for (Map.Entry<String, Card> entry : viewData.getPlayersHandCards().entrySet()) {
      Card card = entry.getValue();
      CardView cardView = new CardView(getContext(), null);
      // TODO: Replace drawable with cards drawable
      cardView.setupWithCard(
          entry.getKey(), card.getPower(), card.getName(), R.drawable.an_craite_amorsmith);
      binding.playersHandLayout.getRoot().addView(cardView);

      playersHandCardViews.add(cardView);
    }
  }

  private void handleEvents(SingleEvent<BoardViewModel.Event> event) {
    BoardViewModel.Event eventValue = event.getValueIfNotHandled();
    if (eventValue == null) return;

    switch (eventValue) {
      case SHOW_MULLIGAN:
        // TODO: Replace with Mulligan Fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.mulligan);
        builder.setNegativeButton(
            R.string.cancel,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                viewModel.cancelMulligan();
              }
            });
        AlertDialog dialog = builder.create();
        dialog.show();

        break;
      default:
        break;
    }
  }
}
