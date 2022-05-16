package at.aau.se2.gwent;

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

import java.util.Objects;

import at.aau.se2.gwent.databinding.MainboardBinding;
import at.aau.se2.gwent.views.Boardviewmodel;
import at.aau.se2.gwent.views.detailedcard.DetailedCardFragment;


public class BoardFragment extends Fragment {
    private static final String TAG = BoardFragment.class.getSimpleName();

    private Boardviewmodel viewModel;
    private MainboardBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // link the viewModel to the fragment
        viewModel = new ViewModelProvider(this).get(Boardviewmodel.class);
        //viewModel.getCurrentState().observe(this, this::updateUI);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = MainboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this should destroy the fragment if the button is clicked
        /*binding.backgroundImageButton.setOnClickListener(
                view -> {
                    Navigation.findNavController(
                            Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
                            .popBackStack();
                });*/
    }
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }



}
