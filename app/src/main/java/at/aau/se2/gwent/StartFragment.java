package at.aau.se2.gwent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    binding.cardView.setupWithCard(4, "WeaponSmith", R.drawable.an_craite_amorsmith);
    binding.buttonFirst.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Log.v(TAG, "DidClick StartGame");
          }
        });
    binding.cardView.setOnLongClickListener(
        new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            Intent i = new Intent(getContext(), PlayingCardDetailed.class);
            startActivity(i);
            return true;
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
