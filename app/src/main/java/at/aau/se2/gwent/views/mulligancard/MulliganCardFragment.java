package at.aau.se2.gwent.views.mulligancard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import at.aau.se2.gamelogic.GameLogic.*;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentMulliganCardBinding;
import at.aau.se2.gwent.views.common.CardView;

/**
 * A simple {@link Fragment} subclass. Use the {@link MulliganCardFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class MulliganCardFragment extends Fragment {
  // private androidx.cardview.widget.CardView getCardsToMulligan

  private MulliganCardViewModel viewModel;
  private FragmentMulliganCardBinding binding;
  private static final String TAG = MulliganCardFragment.class.getSimpleName();

  public MulliganCardFragment() {}
  // private MulliganCardFragment binding;

  public MulliganCardFragment newInstance() {
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

  @Override
  public void onDestroy() {
    super.onDestroy();
    viewModel.didClickCancel();
  }

  private void setUpUI() {
    binding.mulliganCard1.setupWithCard(0, "", R.drawable.card_background);
    binding.mulliganCard2.setupWithCard(0, "", R.drawable.card_background);
    binding.mulliganCard3.setupWithCard(0, "", R.drawable.card_background);
    binding.mulliganCard4.setupWithCard(0, "", R.drawable.card_background);
    binding.mulliganCard5.setupWithCard(0, "", R.drawable.card_background);
    binding.mulliganCard6.setupWithCard(0, "", R.drawable.card_background);
    binding.goBackButton.setOnClickListener(
        view -> {
          viewModel.didClickCancel();
          Navigation.findNavController(
                  Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
              .popBackStack();
        });
  }

  private void updateUI(ArrayList<Card> cards) {
    // fill cardViews
    ArrayList<CardView> mulliganCardViews =
        new ArrayList(
            Arrays.asList(
                binding.mulliganCard1,
                binding.mulliganCard2,
                binding.mulliganCard3,
                binding.mulliganCard4,
                binding.mulliganCard5,
                binding.mulliganCard6));
    for (int i = 0; cards.size() > i; i++) {
      CardView mulliganCardView = mulliganCardViews.get(i);
      Card card = cards.get(i);
      mulliganCardView.setupWithCard(
          card.getPower(), card.getName(), R.drawable.an_craite_amorsmith);
    }
  }
}
