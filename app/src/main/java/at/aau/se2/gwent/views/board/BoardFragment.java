package at.aau.se2.gwent.views.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gamelogic.comunication.SingleEvent;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.databinding.FragmentBoardviewBinding;
import at.aau.se2.gwent.views.common.CardView;

// TODO: Hide NavBar

public class BoardFragment extends Fragment {
  private static final String TAG = BoardFragment.class.getSimpleName();

  private BoardViewModel viewModel;
  private FragmentBoardviewBinding binding;
  private ArrayList<CardView> playersHandCardViews;
  private ArrayList<CardView> opponentsHandCardViews;
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
    playerRowCardViews.put(RowType.MELEE, getCardsFromLayout(binding.playersMeleeRowLayout));
    playerRowCardViews.put(RowType.RANGED, getCardsFromLayout(binding.playersRangeRowLayout));
    opponentRowCardViews.put(RowType.MELEE, getCardsFromLayout(binding.opponentsMeleeRowLayout));
    opponentRowCardViews.put(RowType.RANGED, getCardsFromLayout(binding.opponentsRangeRowLayout));

    playersHandCardViews = getCardsFromLayout(binding.playersHandLayout);
    opponentsHandCardViews = getCardsFromLayout(binding.opponentsHandLayout);
  }

  private ArrayList<CardView> getCardsFromLayout(CardareaBinding layout) {
    int childCount = layout.getRoot().getChildCount();
    ArrayList<CardView> cards = new ArrayList<>();
    for (int i = 0; i < childCount; i++) {
      CardView cardView = (CardView) layout.getRoot().getChildAt(i);
      if (cardView == null) continue;
      cards.add(cardView);
    }
    return cards;
  }

  private void setupButtons() {
    binding.primaryRoundButton.setOnClickListener(
        view -> {
          viewModel.didClickPrimaryButton();
        });
  }

  private void updateUI(BoardViewData viewData) {
    Log.i(TAG, String.valueOf(viewData.getGameField().getRoundNumber()));

    binding.primaryRoundButton.setText(viewData.getPrimaryButtonMode().getText());
    binding.primaryRoundButton.setEnabled(viewData.isPrimaryButtonEnabled());
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
