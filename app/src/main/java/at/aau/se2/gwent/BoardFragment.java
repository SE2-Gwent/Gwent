package at.aau.se2.gwent;

import java.util.ArrayList;
import java.util.Objects;

import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gwent.databinding.MainboardBinding;
import at.aau.se2.gwent.views.BoardViewModel;

public class BoardFragment extends Fragment {
  private static final String TAG = BoardFragment.class.getSimpleName();
  //Deklariere Variablen
  private BoardViewModel viewModel;
  //Binding: nicht immer R.id schreiben muss (Aufruf)
  private MainboardBinding binding;
  //Placeholder und Imageviews zugriff (hole Sie)
  ArrayList<ImageView> cards;
  ArrayList<ImageView> placeholder;

  public BoardFragment() {
    //TODO: connect gamefieldlistener from gamelogic

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ViewModelProvider(this).get(BoardViewModel.class);
    //Observer: Wenn sich der Zustand ändert (Variable), dann führt er die Methode aus
    viewModel.getCurrentState().observe(this, this::updateUI);
  }


  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    //??
    binding = MainboardBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  //??
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);


  }

  //??
  @Override
  public void onStop() {
    super.onStop();
    Objects.requireNonNull(getActivity())
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }

  /*3 Staten:
  Aufruf(Initial) -> Auf Methoden wird zugerufen
  Aufruf(Cardclicked) ->
  Aufruf(Acitivatehero) -> Heroability
  Aufruf(Cardplaced) -> Idee Donebuttom aktivieren (Visible)
  Aufruf(Done) ->

   */
  private void updateUI(BoardViewState state) {
    if(state == null){
      return;
    }
    Log.v(TAG, String.valueOf(state.currentPlayerHandCards));

  }

  //Ruft alle Handkarten auf - (Viewmodel) - setze Karten aus Viewmodel
  private void loadhandcards() {
    ArrayList<Integer> handcards = viewModel.getHandcards();
    for (int i = 0; i < 10; i++) {
      cards.get(i).setImageResource(handcards.get(i));
    }
  }

  //Je nach Status werden die OnClicklistener gesetzt (Handkarte, Board, usw)
  public void setonclicklistener() {
    if (viewModel.getCurrentState().equals(BoardViewState.State.INITIAL)) {
      // onclickaufhandkarten
    } else if (viewModel.getCurrentState().equals(BoardViewState.State.CARDCLICKED)) {
      for (ImageView place : placeholder) {
        //
      }
    } else {
      loadhandcards();
      for (ImageView a : cards) {
        a.setOnClickListener(null);
      }
      for (ImageView b : placeholder) {
        b.setOnClickListener(null);
      }
    }
  }

  //Wenn Karten click erscheinen alle Placeholder
  public void showplaceholders() {
    for (ImageView a : placeholder) {
      a.setVisibility(View.VISIBLE);
    }
  }

  //Ressourcen holen um auf die Imageviews zuzugreifen
  public void generateimageviews() {
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

    //Ressourcen holen um auf die placeholder zuzugreifen
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
