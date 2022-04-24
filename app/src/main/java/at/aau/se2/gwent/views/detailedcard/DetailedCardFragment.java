package at.aau.se2.gwent.views.detailedcard;

import java.util.LinkedList;
import java.util.Objects;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gwent.R;
import at.aau.se2.gwent.data.Card;
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
          Log.v(TAG, "ABILITY clicked.");
        });

    // register onClickListener on close button
    binding.closeButton.setOnClickListener(
        view -> {
          Log.v(TAG, "CLOSE clicked");
        });
  }

  private void updateUI(DetailedCardViewModel.ViewState state) {
    switch (state) {
      case INITIAL:
        // could show loading
        break;
      case LOADED:
        Card currentCard = viewModel.getCard();

        binding.cardName.setText(currentCard.getName());
        binding.cardText.setText(currentCard.getText());
        binding.cardFlavortext.setText(currentCard.getFlavorText());

        setTextViewCardTypes(currentCard.getTypes());
        setTextViewCardPower(currentCard.getPower(), currentCard.getPowerDiff());
        setImageViewCardArtwork(currentCard.getId());
    }
  }

  /*
  convert the list of types into a string
  each type is separated by a comma and a space
   */
  private void setTextViewCardTypes(LinkedList<Card.CardType> types) {
    String str = "";
    for (int i = 0; i < types.size(); i++) {
      str += types.get(i).name();
      if (i != types.size() - 1) {
        str += ", ";
      }
    }
    binding.cardTypes.setText(str);
  }

  /*
  if the card has it's initial power the text color is set to green
  otherwise the text color is set to red
   */
  private void setTextViewCardPower(int power, int powerDiff) {
    binding.cardPower.setText(String.valueOf(power));
    if (powerDiff == 0) {
      binding.cardPower.setTextColor(getResources().getColor(R.color.green, null));
    } else {
      binding.cardPower.setTextColor(getResources().getColor(R.color.red, null));
    }
  }

  /*
  load the card img. from the res directory and apply it to the imageview
  TODO: here we need some unique identifier to get the resource from res
  NOTE: resource names have to start with a letter - this means id doesn't work
   */
  private void setImageViewCardArtwork(int id) {
    Resources res = getResources();

    // retrieve the id of the image - TODO: change argument [name]
    int imgId =
        res.getIdentifier(
            "test_img_to_load",
            "drawable",
            Objects.requireNonNull(DetailedCardFragment.this.getActivity()).getPackageName());
    Log.v(TAG, "imgId retrieved: " + imgId);

    Drawable artwork = ResourcesCompat.getDrawable(res, imgId, null);
    binding.cardImage.setImageDrawable(artwork);
  }
}
