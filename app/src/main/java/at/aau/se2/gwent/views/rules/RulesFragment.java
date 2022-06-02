package at.aau.se2.gwent.views.rules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import at.aau.se2.gwent.databinding.RulesFragmentBinding;

public class RulesFragment extends Fragment {
  private RulesFragmentBinding binding;

  public static RulesFragment newInstance() {
    return new RulesFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = RulesFragmentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    binding.fullRules.loadUrl("file:///android_asset/rules.html");
  }
}
