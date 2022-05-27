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
import at.aau.se2.gamelogic.models.InitialPlayer;
import at.aau.se2.gamelogic.models.RowType;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.databinding.FragmentBoardviewBinding;
import at.aau.se2.gwent.util.CardRowHelper;
import at.aau.se2.gwent.views.common.CardView;
import at.aau.se2.gwent.views.detailedcard.DetailedCardFragment;

// TODO: Hide NavBar

public class BoardFragment extends Fragment implements View.OnClickListener {
  private static final String TAG = BoardFragment.class.getSimpleName();

  private BoardViewModel viewModel;
  private FragmentBoardviewBinding binding;
  private HashMap<String, CardView> playersHandCardViews = new HashMap<>();
  private ArrayList<CardView> opponentsHandCardViews = new ArrayList<>();
  private HashMap<RowType, ArrayList<CardView>> opponentRowCardViews = new HashMap<>();
  private HashMap<RowType, ArrayList<CardView>> playerRowCardViews = new HashMap<>();
  private DetailedCardFragment detailedCardFragment;

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

    for (ArrayList<CardView> cardViews : playerRowCardViews.values()) {
      CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
      CardRowHelper.setCardsOnClickListener(cardViews, this);
    }
    for (ArrayList<CardView> cardViews : opponentRowCardViews.values()) {
      CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
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

    if (viewData.isGameFieldDirty()) {
      updateCurrentHandCardRow(viewData);
      updatePlayerRows(
          viewData,
          binding.opponentsMeleeRowLayout,
          RowType.MELEE,
          opponentRowCardViews,
          InitialPlayer.OPPONENT);
      updatePlayerRows(
          viewData,
          binding.opponentsRangeRowLayout,
          RowType.RANGED,
          opponentRowCardViews,
          InitialPlayer.OPPONENT);
      updatePlayerRows(
          viewData,
          binding.playersMeleeRowLayout,
          RowType.MELEE,
          playerRowCardViews,
          InitialPlayer.INITIATOR);
      updatePlayerRows(
          viewData,
          binding.playersRangeRowLayout,
          RowType.RANGED,
          playerRowCardViews,
          InitialPlayer.INITIATOR);
    }

    boolean cardIsSelected = viewData.getSelectedCardId() != null;
    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.MELEE), cardIsSelected ? View.VISIBLE : View.INVISIBLE);
    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.RANGED), cardIsSelected ? View.VISIBLE : View.INVISIBLE);

    if (viewModel.getOldViewData() != null
        && viewModel.getOldViewData().getSelectedCardId() != null) {
      playersHandCardViews.get(viewModel.getOldViewData().getSelectedCardId()).setSelected(false);
    }
    if (viewData.getSelectedCardId() != null) {
      playersHandCardViews.get(viewData.getSelectedCardId()).setSelected(true);
    }
  }

  private void updateCurrentHandCardRow(BoardViewData viewData) {
    CardRowHelper.removeCardViews(binding.playersHandLayout);
    playersHandCardViews.clear();
    for (Map.Entry<String, Card> entry : viewData.getPlayersHandCards().entrySet()) {
      Card card = entry.getValue();
      if (card == null) continue;

      CardView cardView = new CardView(getContext(), null);
      // TODO: Replace drawable with cards drawable
      cardView.setupWithCard(
          entry.getKey(), card.getPower(), card.getName(), R.drawable.an_craite_amorsmith);
      binding.playersHandLayout.getRoot().addView(cardView);
      cardView.setOnClickListener(
          view -> {
            CardView clickedCardView = (CardView) view;
            viewModel.didClickHandCard(clickedCardView.getCardId());
          });

      cardView.setOnLongClickListener(
          view -> {
            CardView clickedCardView = (CardView) view;
            showDetailOverlay(clickedCardView.getCardId());
            return true;
          });

      playersHandCardViews.put(card.getFirebaseId(), cardView);
    }
  }

  private void updatePlayerRows(
      BoardViewData viewData,
      CardareaBinding rowLayout,
      RowType type,
      HashMap<RowType, ArrayList<CardView>> cardRows,
      InitialPlayer player) {

    ArrayList<Card> row = viewData.getGameField().getRows().meleeRowFor(player);
    if (type == RowType.RANGED) row = viewData.getGameField().getRows().rangedRowFor(player);

    int index = -1;
    for (Card card : row) {
      index++;
      if (card == null) continue;

      CardView cardView = new CardView(getContext(), null);
      // TODO: Replace drawable with cards drawable
      cardView.setupWithCard(
          card.getFirebaseId(), card.getPower(), card.getName(), R.drawable.an_craite_amorsmith);
      rowLayout.getRoot().removeViewAt(index);
      rowLayout.getRoot().addView(cardView, index);
      cardView.setOnClickListener(
          view -> {
            CardView clickedCardView = (CardView) view;
            viewModel.didClickRowCard(clickedCardView.getCardId());
          });

      cardView.setOnLongClickListener(
          view -> {
            CardView clickedCardView = (CardView) view;
            showDetailOverlay(clickedCardView.getCardId());
            return true;
          });

      cardRows.get(type).remove(index);
      cardRows.get(type).add(index, cardView);
    }

    rowLayout.getRoot().forceLayout();
  }

  // onClickListener of players Rows
  @Override
  public void onClick(View view) {
    if (viewModel.getCurrentState().getValue().getSelectedCardId() == null) {
      // Selected State
    }

    // playing card to that location
    RowType rowType = RowType.MELEE;
    if (playerRowCardViews.get(RowType.RANGED).contains(view)) rowType = RowType.RANGED;
    int location = playerRowCardViews.get(rowType).indexOf((CardView) view);
    viewModel.playSelectedCard(rowType, location);
  }

  private void showDetailOverlay(String cardId) {
    if (detailedCardFragment == null) {
      detailedCardFragment = DetailedCardFragment.newInstance();
      detailedCardFragment.setListener(
          new DetailedCardFragment.Listener() {
            @Override
            public void didClickDetailCardFragment() {
              if (detailedCardFragment == null) return;
              getChildFragmentManager().beginTransaction().remove(detailedCardFragment).commit();
            }
          });
    }

    // TODO: configure DetailFragment with card data
    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.overlayFrameLayout, detailedCardFragment)
        .commit();
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
