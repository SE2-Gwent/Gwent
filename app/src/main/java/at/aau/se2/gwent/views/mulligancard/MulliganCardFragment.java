package at.aau.se2.gwent.views.mulligancard;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gamelogic.GameLogic.*;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentMulliganCardBinding;
import at.aau.se2.gwent.views.common.CardView;
import at.aau.se2.gwent.views.common.FragmentBackPressListener;

/**
 * A simple {@link Fragment} subclass. Use the {@link MulliganCardFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class MulliganCardFragment extends Fragment implements FragmentBackPressListener {
  // private androidx.cardview.widget.CardView getCardsToMulligan

  private MulliganCardViewModel viewModel;
  private FragmentMulliganCardBinding binding;
  private static final String TAG = MulliganCardFragment.class.getSimpleName();
  private Listener listener;

  public MulliganCardFragment() {}
  // private MulliganCardFragment binding;

  public static MulliganCardFragment newInstance() {
    // Required empty public constructor
    return new MulliganCardFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(MulliganCardViewModel.class);
    viewModel.getCurrentState().observe(this, this::updateUI);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentMulliganCardBinding.inflate(inflater, container, false);
    setUpUI();
    return binding.getRoot();
  }

  // TODO Runden checken wieviel Cards angezeigt werden. (R0-6K, R1&2-3K)✅  visibility gone
  private void setUpUI() {
    if (viewModel.roundCounter() == 0) {
      binding.mulliganCard1.showAsPlaceholder(false);
      binding.mulliganCard2.showAsPlaceholder(false);
      binding.mulliganCard3.showAsPlaceholder(false);
      binding.mulliganCard4.showAsPlaceholder(false);
      binding.mulliganCard5.showAsPlaceholder(false);
      binding.mulliganCard6.showAsPlaceholder(false);
      binding.goBackButton.setOnClickListener(
          view -> {
            viewModel.didClickCancel();
            notifyClose();
          });
      binding.mulliganCard1.setOnClickListener(mulliganCardListener);
      binding.mulliganCard2.setOnClickListener(mulliganCardListener);
      binding.mulliganCard3.setOnClickListener(mulliganCardListener);
      binding.mulliganCard4.setOnClickListener(mulliganCardListener);
      binding.mulliganCard5.setOnClickListener(mulliganCardListener);
      binding.mulliganCard6.setOnClickListener(mulliganCardListener);

    } else if (viewModel.roundCounter() == 1 || viewModel.roundCounter() == 2) {
      binding.mulliganCard1.showAsPlaceholder(false);
      binding.mulliganCard2.showAsPlaceholder(false);
      binding.mulliganCard3.showAsPlaceholder(false);
      binding.goBackButton.setOnClickListener(
          view -> {
            viewModel.didClickCancel();
            notifyClose();
          });
      binding.mulliganCard1.setOnClickListener(mulliganCardListener);
      binding.mulliganCard2.setOnClickListener(mulliganCardListener);
      binding.mulliganCard3.setOnClickListener(mulliganCardListener);
    }
  }

  private View.OnClickListener mulliganCardListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          CardView cardView = (CardView) view;
          viewModel.didClickCard(Integer.parseInt(cardView.getCardId()));
        }
      };

  private void updateUI(ArrayList<Card> cards) {
    if (cards == null) { // early returns
      return;
    }
    // fill cardViews
    ArrayList<CardView> mulliganCardViews = null;

    if (viewModel.roundCounter() == 0) {
      mulliganCardViews =
          new ArrayList(
              Arrays.asList(
                  binding.mulliganCard1,
                  binding.mulliganCard2,
                  binding.mulliganCard3,
                  binding.mulliganCard4,
                  binding.mulliganCard5,
                  binding.mulliganCard6));

    } else if (viewModel.roundCounter() == 1 || viewModel.roundCounter() == 2) {
      mulliganCardViews =
          new ArrayList(
              Arrays.asList(binding.mulliganCard1, binding.mulliganCard2, binding.mulliganCard3));
    }
    if (mulliganCardViews == null) {
      return;
    }
    for (int i = 0; cards.size() > i; i++) {
      Card card = cards.get(i);
      Context context = getActivity().getApplicationContext();
      Resources resources = context.getResources();
      int resourceId =
          resources.getIdentifier(card.getImgResourceBasic(), "drawable", context.getPackageName());

      if (resourceId == 0) {
        resourceId = R.drawable.card_background;
      }

      CardView mulliganCardView = mulliganCardViews.get(i);
      mulliganCardView.setupWithCard(
          String.valueOf(card.getId()),
          card.getPower(),
          card.getPowerDiff(),
          card.getName(),
          resourceId);
    }
    // TODO sollte sich schließen. (transfer to UpdateUI)✅
    if (!viewModel.hasMulligansLeft()) {
      notifyClose();
    }
    binding.mulliganCountByLogic.setText(viewModel.mulligansLeftText());
  }

  @Override
  public void onBackPressed() {
    viewModel.didClickCancel();
  }

  public void setListener(MulliganCardFragment.Listener listener) {
    this.listener = listener;
  }

  public interface Listener {
    void didClickCancel();
  }

  private void notifyClose() {
    if (listener == null) {
      return;
    }
    listener.didClickCancel();
  }
}
// TODO
// textview -> updateUI aktualiseren via mulligansLeft
// binding.textview = mulligansLeft(); -> Nummeranzeige im Cardview? -> Kevin fragen
