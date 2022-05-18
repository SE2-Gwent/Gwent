package at.aau.se2.gwent;

import java.util.ArrayList;
import java.util.Objects;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
  ArrayList<ImageView> cards;
  ArrayList<ImageView> placeholder;

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
        generateimageviews();
        loadhandcards();
        setonclicklistener();
        break;
      case CARDCLICKED:
        showplaceholders();
        setonclicklistener();
        break;
      case DONE:
        loadhandcards();
        setonclicklistener();
    }
  }

  private void loadhandcards() {
    ArrayList<Integer> handcards = viewModel.getHandcards();
    for(int i= 0; i<10; i++){
      cards.get(i).setImageResource(handcards.get(i));
    }
  }
  public void setonclicklistener(){
    if(viewModel.getCurrentState().equals(Board.State.INITIAL)){
      //onclickaufhandkarten
    }else if(viewModel.getCurrentState().equals(Board.State.CARDCLICKED)){
        for(ImageView place: placeholder){
          //
        }
    }else{
      loadhandcards();
      for (ImageView a: cards){
        a.setOnClickListener(null);
      }
      for (ImageView b: placeholder){
        b.setOnClickListener(null);
      }
    }

  }

  public void showplaceholders(){
      for(ImageView a: placeholder){
        a.setVisibility(View.VISIBLE);
      }
  }

  public void generateimageviews(){
    cards = new ArrayList<ImageView>(10);
    cards.add(binding.handdeck.card1);
    cards.add(binding.handdeck.card2);
    cards.add(binding.handdeck.card3);
    cards.add(binding.handdeck.card4);
    cards.add(binding.handdeck.card5);
    cards.add(binding.handdeck.card6);
    cards.add(binding.handdeck.card7);
    cards.add(binding.handdeck.card8);
    cards.add(binding.handdeck.card9);
    cards.add(binding.handdeck.card10);

    placeholder = new ArrayList<>(18);
    placeholder.add(binding.firstrow.rowcard1);
    placeholder.add(binding.firstrow.rowcard2);
    placeholder.add(binding.firstrow.rowcard3);
    placeholder.add(binding.firstrow.rowcard4);
    placeholder.add(binding.firstrow.rowcard5);
    placeholder.add(binding.firstrow.rowcard6);
    placeholder.add(binding.firstrow.rowcard7);
    placeholder.add(binding.firstrow.rowcard8);
    placeholder.add(binding.firstrow.rowcard9);
    placeholder.add(binding.secondrow.rowcard1);
    placeholder.add(binding.secondrow.rowcard2);
    placeholder.add(binding.secondrow.rowcard3);
    placeholder.add(binding.secondrow.rowcard4);
    placeholder.add(binding.secondrow.rowcard5);
    placeholder.add(binding.secondrow.rowcard6);
    placeholder.add(binding.secondrow.rowcard7);
    placeholder.add(binding.secondrow.rowcard8);
    placeholder.add(binding.secondrow.rowcard9);
  }

}
