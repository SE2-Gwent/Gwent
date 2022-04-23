package at.aau.se2.gwent.views.detailedcard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import at.aau.se2.gwent.databinding.DetailedCardFragmentBinding;

public class DetailedCardFragment extends Fragment {
    private static final String TAG = DetailedCardFragment.class.getSimpleName();

    private DetailedCardViewModel viewModel;
    private DetailedCardFragmentBinding binding;

    public static DetailedCardFragment newInstance() {
        return new DetailedCardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel =
                new ViewModelProvider(this).get(DetailedCardViewModel.class); // link viewModel to fragment
        viewModel.getCurrentState().observe(this, this::updateUI);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding =
                DetailedCardFragmentBinding.inflate(inflater, container, false); // parse detailed_card.xml
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // register onClickListener on ability button
        binding.abilityButton.setOnClickListener(
                view -> {
                    Log.v(TAG, "Ability activated.");
                });
    }

    private void updateUI(DetailedCardViewModel.ViewState state) {
        switch (state) {
            case INITIAL:
                // could show loading
                break;
            case LOADED:
                // TODO: set the views according to the data within the viewModel
        }
    }
}
