package at.aau.se2.gwent.views.mulligancard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import at.aau.se2.gamelogic.GameLogic.*;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.databinding.FragmentMulliganCardBinding;
import at.aau.se2.gwent.views.common.CardView;
import at.aau.se2.gwent.views.detailedcard.DetailedCardViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MulliganCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MulliganCardFragment extends Fragment {
    //private androidx.cardview.widget.CardView getCardsToMulligan

    private MulliganCardViewModel viewModel;
    private FragmentMulliganCardBinding binding;

    public MulliganCardFragment() {
    }
    //private MulliganCardFragment binding;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMulliganCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    private void updateUI(ArrayList<Card> cards) {
        //fill cardViews

        viewModel.state.getValue();

        //if cardCount is 3 -> only have 3 rdm cards visible/clickable -> clicked -> return to game
        if (GameLogic.getCardsToMulligan == 3) {

            binding.mulliganCard1.setVisibility(View.GONE);
            binding.mulliganCard2.setVisibility(View.GONE);
            binding.mulliganCard3.setVisibility(View.GONE);
            binding.mulliganCard4.setVisibility(View.VISIBLE);
            binding.mulliganCard5.setVisibility(View.VISIBLE);
            binding.mulliganCard6.setVisibility(View.VISIBLE);

            // else if cardCount is 6 -> have 6 rdm cards visible/clickable -> clicked -> return to game
        } else if (GameLogic.getCardsToMulligan == 6) {
            // public MutableLiveData<ViewState> state = new MutableLiveData<>();

            binding.mulliganCard1.setVisibility(View.VISIBLE);
            binding.mulliganCard2.setVisibility(View.VISIBLE);
            binding.mulliganCard3.setVisibility(View.VISIBLE);
            binding.mulliganCard4.setVisibility(View.VISIBLE);
            binding.mulliganCard5.setVisibility(View.VISIBLE);
            binding.mulliganCard6.setVisibility(View.VISIBLE);


            //if goBackButton ('Cancel') is clicked -> return to game
        } else {
            binding.goBackButton.setOnClickListener(View );

        }
        //Cardviews befüllen
        // Cardviews hide/show (3/6)

        mulli.setForeground(isSelected ? borderDrawable : null); // ? = Tenary Operator

    }
}
