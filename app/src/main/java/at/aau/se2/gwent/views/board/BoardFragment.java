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
import androidx.navigation.Navigation;
import at.aau.se2.gamelogic.GameLogic;
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
  private HashMap<String, CardView> opponentsHandCardViews = new HashMap<>();
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
  public void onResume() {
    super.onResume();
    if (viewModel.getCurrentState().getValue() == null) return;
    updateUI(viewModel.getCurrentState().getValue());
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
    binding
        .pointView
        .getMainButton()
        .setOnClickListener(
            view -> {
              viewModel.didClickPrimaryButton();
            });

    binding.debugButton.setOnClickListener(
        view -> {
          Navigation.findNavController(
                  Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
              .navigate(R.id.action_board_fragment_to_game_debug_fragment);
        });
  }

  private void updateUI(BoardViewData viewData) {
    if (viewData.isGameFieldDirty()) {
      updateCurrentHandCardRow(
          viewData.getPlayersHandCards(), binding.playersHandLayout, playersHandCardViews, false);
      updateCurrentHandCardRow(
          viewData.getOpponentsHandCards(),
          binding.opponentsHandLayout,
          opponentsHandCardViews,
          true);
      updatePlayerRows(
          viewData,
          binding.opponentsMeleeRowLayout,
          RowType.MELEE,
          opponentRowCardViews,
          viewData.getWhoAmI().other());
      updatePlayerRows(
          viewData,
          binding.opponentsRangeRowLayout,
          RowType.RANGED,
          opponentRowCardViews,
          viewData.getWhoAmI().other());
      updatePlayerRows(
          viewData,
          binding.playersMeleeRowLayout,
          RowType.MELEE,
          playerRowCardViews,
          viewData.getWhoAmI());
      updatePlayerRows(
          viewData,
          binding.playersRangeRowLayout,
          RowType.RANGED,
          playerRowCardViews,
          viewData.getWhoAmI());
    }

    boolean cardIsSelected = viewData.getSelectedCardId() != null;
    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.MELEE), cardIsSelected ? View.VISIBLE : View.INVISIBLE);
    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.RANGED), cardIsSelected ? View.VISIBLE : View.INVISIBLE);

    if (viewModel.getOldViewData() != null
        && viewModel.getOldViewData().getSelectedCardId() != null) {
      playersHandCardViews.get(viewModel.getOldViewData().getSelectedCardId()).setSelected(false);
      // TODO: selection for rowCardViews
    }
    if (viewData.getSelectedCardId() != null) {
      playersHandCardViews.get(viewData.getSelectedCardId()).setSelected(true);
      // TODO: selection for rowCardViews
    }

    binding.pointView.bind(viewData);
  }

  private void updateCurrentHandCardRow(
      HashMap<String, Card> cards,
      CardareaBinding handLayout,
      HashMap<String, CardView> handCardViews,
      boolean isOpponent) {
    if (cards == null) return;

    CardRowHelper.removeCardViews(handLayout);
    handCardViews.clear();
    for (Map.Entry<String, Card> entry : cards.entrySet()) {
      Card card = entry.getValue();
      if (card == null) continue;

      CardView cardView = new CardView(getContext(), null);
      handLayout.getRoot().addView(cardView);
      // TODO: Replace drawable with cards drawable
      if (isOpponent) {
        cardView.showAsPlaceholder(false);
      } else {
        cardView.setupWithCard(
            entry.getKey(), card.getPower(), card.getName(), R.drawable.aguara_basic);
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
      }

      handCardViews.put(card.getFirebaseId(), cardView);
    }
  }

  private void updatePlayerRows(
      BoardViewData viewData,
      CardareaBinding rowLayout,
      RowType type,
      HashMap<RowType, ArrayList<CardView>> cardRows,
      InitialPlayer player) {

    HashMap<String, Card> row = viewData.getGameField().getRows().meleeRowFor(player);
    if (type == RowType.RANGED) row = viewData.getGameField().getRows().rangedRowFor(player);

    for (int i = 0; i < GameLogic.HAND_CARD_NUMBER; i++) {
      Card card = row.get(i + "_index");

      if (card == null) continue;

      CardView cardView = new CardView(getContext(), null);
      // TODO: Replace drawable with cards drawable
      cardView.setupWithCard(
          card.getFirebaseId(), card.getPower(), card.getName(), R.drawable.aguara_basic);
      rowLayout.getRoot().removeViewAt(i);
      rowLayout.getRoot().addView(cardView, i);
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

      cardRows.get(type).remove(i);
      cardRows.get(type).add(i, cardView);
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
