package at.aau.se2.gwent.views.detailedcard;

import java.util.ArrayList;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import at.aau.se2.gamelogic.models.CardType;
import at.aau.se2.gwent.R;
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
    // link the viewModel to the fragment
    viewModel = new ViewModelProvider(this).get(DetailedCardViewModel.class);
    viewModel.getCurrentState().observe(this, this::updateUI);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = DetailedCardFragmentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // this should destroy the fragment if the button is clicked
    binding.backgroundImageButton.setOnClickListener(
        view -> {
          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
          fragmentManager.beginTransaction().remove(this).commit();
        });
  }

  private void updateUI(DetailedCardViewModel.ViewState state) {
    switch (state) {
      case INITIAL:
        // could show loading
        break;
      case LOADED:
        updateColorCardPower(viewModel.getCard().getPowerDiff());
        setImg(viewModel.getImgResourceName());

        binding.cardName.setText(viewModel.getCard().getName());
        binding.cardText.setText(viewModel.getCard().getCardText());
        binding.cardPower.setText(String.valueOf(viewModel.getCard().getPower()));

        // we have to convert the enum-array to a String, before setting the textView
        setTextViewCardTypes(viewModel.getCard().getTypes());
    }
  }

  // update textColor of textView power according to powerDiff
  private void updateColorCardPower(int powerDiff) {
    if (powerDiff == 0) {
      binding.cardPower.setTextColor(getResources().getColor(R.color.green, null));
    } else {
      binding.cardPower.setTextColor(getResources().getColor(R.color.red, null));
    }
  }

  // set textView cardTypes (convert Array of enums to string)
  private void setTextViewCardTypes(ArrayList<CardType> types) {
    String typesText = "";
    for (int i = 0; i < types.size(); i++) {
      String tmp = types.get(i).toString();
      typesText += tmp.charAt(0) + tmp.substring(1).toLowerCase();
      if (i != types.size() - 1) {
        typesText += ", ";
      }
    }
    binding.cardTypes.setText(typesText);
  }

  // load the img from res. and assign it to the imageButton
  private void setImg(String imgResource) {
    Resources res = getResources();

    int imgId =
        res.getIdentifier(
            imgResource,
            "drawable",
            Objects.requireNonNull(DetailedCardFragment.this.getActivity()).getPackageName());
    Log.v(TAG, "imgId retrieved: " + imgId);

    Drawable artwork = ResourcesCompat.getDrawable(res, imgId, null);
    binding.backgroundImageButton.setImageDrawable(artwork);
  }
}
