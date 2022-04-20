package at.aau.se2.gwent.views.detailedcard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.aau.se2.gwent.R;

public class DetailedCardFragment extends Fragment {

    private DetailedCardViewModel viewModel;

    public static DetailedCardFragment newInstance() {
        return new DetailedCardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detailed_card_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DetailedCardViewModel.class);
        // TODO: Use the ViewModel
    }
}