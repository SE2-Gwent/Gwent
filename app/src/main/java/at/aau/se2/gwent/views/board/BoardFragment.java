package at.aau.se2.gwent.views.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
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
import at.aau.se2.gwent.Environment;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.databinding.FragmentBoardviewBinding;
import at.aau.se2.gwent.util.CardRowHelper;
import at.aau.se2.gwent.views.common.CardView;
import at.aau.se2.gwent.views.detailedcard.DetailedCardFragment;
import at.aau.se2.gwent.views.mulligancard.MulliganCardFragment;

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
  private MulliganCardFragment mulliganCardFragment;

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
    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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

    CardRowHelper.removeAllViews(binding.playersHandLayout);
    CardRowHelper.removeAllViews(binding.opponentsHandLayout);
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
          Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
              .navigate(R.id.action_board_fragment_to_game_debug_fragment);
        });

    binding.currentHeroButton.setOnClickListener(
        view -> {
          viewModel.didClickHero();
        });

    binding.opponentHeroButton.setOnClickListener(
        view -> {
          // we could show here some information about hero
          Toast.makeText(
                  getContext(),
                  viewModel.getCurrentState().getValue().getMyHeroView().other().getAlertText(),
                  Toast.LENGTH_SHORT)
              .show();
        });
  }

  private void resetCardRowsToInitialState() {
    for (ArrayList<CardView> cardViews : playerRowCardViews.values()) {
      CardRowHelper.makeAllPlaceholder(cardViews);
      CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
      CardRowHelper.setCardsOnClickListener(cardViews, this);
    }
    for (ArrayList<CardView> cardViews : opponentRowCardViews.values()) {
      CardRowHelper.makeAllPlaceholder(cardViews);
      CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
    }
  }

  private void updateUI(BoardViewData viewData) {
    if (viewData.isGameFieldDirty()) {
      resetCardRowsToInitialState();
      updateCurrentHandCardRow(
          viewData.getPlayersHandCards(), binding.playersHandLayout, playersHandCardViews, false);

      // clean rows ...
      for (ArrayList<CardView> cardViews : playerRowCardViews.values()) {
        CardRowHelper.makeAllPlaceholder(cardViews);
        CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
        CardRowHelper.setCardsOnClickListener(cardViews, this);
      }
      for (ArrayList<CardView> cardViews : opponentRowCardViews.values()) {
        CardRowHelper.makeAllPlaceholder(cardViews);
        CardRowHelper.setCardsVisibilityForPlaceholders(cardViews, View.INVISIBLE);
      }

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

    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.MELEE),
        viewData.shouldShowCardPlaceholders() ? View.VISIBLE : View.INVISIBLE);
    CardRowHelper.setCardsVisibilityForPlaceholders(
        playerRowCardViews.get(RowType.RANGED),
        viewData.shouldShowCardPlaceholders() ? View.VISIBLE : View.INVISIBLE);

    if (viewModel.getOldViewData() != null
        && viewModel.getOldViewData().getSelectedCardId() != null
        && playersHandCardViews.containsKey(viewModel.getOldViewData().getSelectedCardId())) {
      playersHandCardViews.get(viewModel.getOldViewData().getSelectedCardId()).setSelected(false);
      // TODO: selection for rowCardViews
    }
    if (viewData.getSelectedCardId() != null) {
      playersHandCardViews.get(viewData.getSelectedCardId()).setSelected(true);
      // TODO: selection for rowCardViews
    }

    binding.currentHeroButton.setBackground(
        getResources().getDrawable(viewData.getMyHeroView().backgroundDrawable()));
    binding.opponentHeroButton.setBackground(
        getResources().getDrawable(viewData.getMyHeroView().other().backgroundDrawable()));
    binding.currentHeroButton.setEnabled(viewData.isHeroEnabled());
    binding.opponentHeroButton.setEnabled(viewData.isOpponentHeroEnabled());
    binding.pointView.bind(viewData);
  }

  private void updateCurrentHandCardRow(
      HashMap<String, Card> cards,
      CardareaBinding handLayout,
      HashMap<String, CardView> handCardViews,
      boolean isOpponent) {
    if (cards == null) return;

    CardRowHelper.removeAllViews(handLayout);
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
        final int imageRessourceID =
            getResources()
                .getIdentifier(
                    card.getImgResourceBasic(), "drawable", getContext().getPackageName());
        cardView.setupWithCard(
            entry.getKey(),
            card.getCurrentAttackPoints(),
            card.getPowerDiff(),
            card.getName(),
            imageRessourceID);
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
      final int imageRessourceID =
          getResources()
              .getIdentifier(card.getImgResourceBasic(), "drawable", getContext().getPackageName());
      cardView.setupWithCard(
          card.getFirebaseId(),
          card.getCurrentAttackPoints(),
          card.getPowerDiff(),
          card.getName(),
          imageRessourceID);
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
      Bundle args = new Bundle();
      args.putString("key_0", cardId);
      detailedCardFragment.setArguments(args);

      detailedCardFragment.setListener(
          new DetailedCardFragment.Listener() {
            @Override
            public void didClickDetailCardFragment() {
              if (detailedCardFragment == null) return;
              getChildFragmentManager().beginTransaction().remove(detailedCardFragment).commit();
            }
          });
    } else {
      // detailedCardFragment.updateCardId(cardId);
      Bundle args = new Bundle();
      args.putString("key_0", cardId);
      detailedCardFragment.setArguments(args);
    }

    if (detailedCardFragment.isAdded()) return;

    getChildFragmentManager()
        .beginTransaction()
        .add(R.id.overlayFrameLayout, detailedCardFragment)
        .commit();
  }

  private void handleEvents(SingleEvent<BoardViewModel.Event> event) {
    if (mulliganCardFragment == null) {
      mulliganCardFragment = MulliganCardFragment.newInstance();
      mulliganCardFragment.setListener(
          new MulliganCardFragment.Listener() {
            @Override
            public void didClickCancel() {
              if (mulliganCardFragment == null) return;
              getChildFragmentManager().beginTransaction().remove(mulliganCardFragment).commit();
            }
          });
    }
    BoardViewModel.Event eventValue = event.getValueIfNotHandled();
    if (eventValue == null) return;

    switch (eventValue) {
      case SHOW_MULLIGAN:
        getChildFragmentManager()
            .beginTransaction()
            .add(R.id.overlayFrameLayout, mulliganCardFragment)
            .commit();
        break;

      case VIBRATE:
        Vibrator v = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
          // deprecated in API 26
          v.vibrate(200);
        }
        break;

      case SHOW_WINNER:
        showWinner();
        break;

      default:
        break;
    }
  }

  private void showWinner() {
    if (viewModel.haveIWon() == null) return;

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.game_ended);
    builder.setCancelable(false);

    builder.setMessage(viewModel.haveIWon() ? R.string.you_have_won : R.string.opponents_has_won);
    builder.setNegativeButton(
        R.string.ok,
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            Environment.getSharedInstance().resetGameLogic();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .popBackStack();
          }
        });
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
