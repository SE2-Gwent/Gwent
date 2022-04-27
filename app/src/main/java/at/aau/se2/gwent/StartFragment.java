package at.aau.se2.gwent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import at.aau.se2.gwent.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
  private static final String TAG = StartFragment.class.getSimpleName();

  private FragmentStartBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentStartBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.buttonFirst.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Log.v(TAG, "DidClick StartGame");
          }
        });
    binding.playingcardBasic.setOnLongClickListener(
        new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            Intent i = new Intent(getContext(), PlayingCardDetailed.class);
            startActivity(i);
            return true;
          }
        });

    binding.playingcardBasic.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            boolean cardIsSelected = true; // TODO - Change
            if (cardIsSelected) {
              Animation animation =
                  new ScaleAnimation(
                      1.2f,
                      1.0f,
                      1.2f,
                      1.0f,
                      Animation.RELATIVE_TO_SELF,
                      0.5f,
                      Animation.RELATIVE_TO_SELF,
                      0.5f);
              animation.setFillAfter(true);
              animation.setDuration(500);
              binding.playingcardBasic.startAnimation(animation);
            } else {
              Animation animation =
                  new ScaleAnimation(
                      1.0f,
                      1.2f,
                      1.0f,
                      1.2f,
                      Animation.RELATIVE_TO_SELF,
                      0.5f,
                      Animation.RELATIVE_TO_SELF,
                      0.5f);
              animation.setFillAfter(true);
              animation.setDuration(500);
              binding.playingcardBasic.startAnimation(animation);
            }
            cardIsSelected = !cardIsSelected;
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
