package at.aau.se2.gwent.views.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import at.aau.se2.gamelogic.state.GameState;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.ViewPointBinding;
import at.aau.se2.gwent.views.board.BoardViewData;

public class PointView extends FrameLayout {
  private ViewPointBinding binding;

  public PointView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PointView(Context context, AttributeSet attrs, int defStyleattr) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    binding = ViewPointBinding.inflate(LayoutInflater.from(context));
    addView(binding.getRoot());
  }

  public void bind(BoardViewData viewData) {
    binding.mainButton.setText(viewData.getPrimaryButtonMode().getText());
    binding.mainButton.setEnabled(viewData.isPrimaryButtonEnabled());

    binding.turnTextView.setText(
        viewData.isMyTurn()
            ? getContext().getString(R.string.your_turn)
            : getContext().getString(R.string.opponents_turn));
    binding.roundTextView.setText(
        String.format("%s %s", getResources().getText(R.string.round), viewData.getRoundNumber()));
    if (viewData.getCurrentGameState() == GameState.WAIT_FOR_OPPONENT) {
      binding.turnTextView.setText(R.string.waiting_for_opponent);
      binding.roundTextView.setText(
          String.format("%s %s", getResources().getText(R.string.game_id), viewData.getGameId()));
    }

    binding.currentPlayerPoints.setText(viewData.getCurrentPlayersPoints());
    binding.opponentPlayerPoints.setText(viewData.getOpponentPoints());

    binding.currentPlayerFirstRoundWon.setEnabled(viewData.getCurrentPlayerRoundsWon() > 0);
    binding.currentPlayerSecondRoundWon.setEnabled(viewData.getCurrentPlayerRoundsWon() > 1);
    binding.opponentFirstRoundWon.setEnabled(viewData.getOpponentRoundsWon() > 0);
    binding.opponentSecondRoundWon.setEnabled(viewData.getOpponentRoundsWon() > 1);
  }

  public Button getMainButton() {
    return binding.mainButton;
  }
}
