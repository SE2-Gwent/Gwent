package at.aau.se2.gwent.util;

import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.views.common.CardView;

public class CardRowHelper {
  public static void setCardsAlpha(ArrayList<CardView> cardViews, float alpha) {
    float bound = Math.max(0, Math.min(alpha, 1)); // only floats between 0.0 and 1.0
    for (CardView cardView : cardViews) {
      cardView.setAlpha(bound);
    }
  }

  public static void setCardsVisibility(ArrayList<CardView> cardViews, int visibility) {
    for (CardView cardView : cardViews) {
      cardView.setVisibility(visibility);
    }
  }

  public static void setBackgroundDrawable(CardareaBinding[] bindings, @DrawableRes int drawable) {
    for (CardareaBinding binding : bindings) {
      binding.getRoot().setBackgroundResource(drawable);
    }
  }
}
