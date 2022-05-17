package at.aau.se2.gwent;

import java.util.ArrayList;
import java.util.Objects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gwent.databinding.MainboardBinding;
import at.aau.se2.gwent.views.Boardviewmodel;

public class BoardFragment extends Fragment {
  private static final String TAG = BoardFragment.class.getSimpleName();

  private Boardviewmodel viewModel;
  private MainboardBinding binding;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(Boardviewmodel.class);
    viewModel.getCurrentState().observe(this, this::updateUI);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

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

  private void updateUI(Board.State state) {
    switch (state) {
      case INITIAL:
        loadhandcards();
        setonclicklistener();
        break;
      case CARDCLICKED:
        showplaceholders();
        break;
      case DONE:
    }
  }

  private void loadhandcards() {
    ArrayList<Integer> handcards = viewModel.getHandcards();
    binding.handdeck.card1.setImageResource(R.drawable.hearts2);
    binding.handdeck.card2.setImageResource(R.drawable.hearts2);
    binding.handdeck.card3.setImageResource(R.drawable.hearts2);
    binding.handdeck.card4.setImageResource(R.drawable.hearts2);
    binding.handdeck.card5.setImageResource(R.drawable.hearts2);
    binding.handdeck.card6.setImageResource(R.drawable.hearts2);
    binding.handdeck.card7.setImageResource(R.drawable.hearts2);
    binding.handdeck.card8.setImageResource(R.drawable.hearts2);
    binding.handdeck.card9.setImageResource(R.drawable.hearts2);
    binding.handdeck.card10.setImageResource(0);
  }
  public void setonclicklistener(){

  }

  public void showplaceholders(){

  }
}
