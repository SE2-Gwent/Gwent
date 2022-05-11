package at.aau.se2.gwent.views.detailedcard;

import java.util.Objects;

import android.content.pm.ActivityInfo;
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
import androidx.navigation.Navigation;
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
    Objects.requireNonNull(getActivity())
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    binding = DetailedCardFragmentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // this should destroy the fragment if the button is clicked
    binding.backgroundImageButton.setOnClickListener(
        view -> {
          Navigation.findNavController(
                  Objects.requireNonNull(getActivity()), R.id.nav_host_fragment_content_main)
              .popBackStack();
        });
  }

  @Override
  public void onStop() {
    super.onStop();
    Objects.requireNonNull(getActivity())
        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }

  private void updateUI(CardDetails.ViewState state) {
    switch (state) {
      case INITIAL:
        // could show loading
        break;
      case LOADED:
        updateColorCardPower(viewModel.getCard().getPowerDiff());
        setImg(viewModel.getCard().getImgResourceName());

        binding.cardName.setText(viewModel.getCard().getName());
        binding.cardTypes.setText(viewModel.getCard().getTypes());
        binding.cardText.setText(viewModel.getCard().getCardText());
        binding.cardPower.setText(String.valueOf(viewModel.getCard().getPower()));
    }
  }

  // update textColor of textView power according to powerDiff
  private void updateColorCardPower(int powerDiff) {
    int color = powerDiff == 0 ? R.color.green : R.color.red;
    binding.cardPower.setTextColor(getResources().getColor(color, null));
  }

  // load the img from res. and assign it to the imageButton
  protected void setImg(String imgResource) {
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
