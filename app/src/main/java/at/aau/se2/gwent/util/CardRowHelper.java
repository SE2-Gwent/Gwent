package at.aau.se2.gwent.util;

import java.util.ArrayList;

import android.view.View;
import at.aau.se2.gwent.databinding.CardareaBinding;
import at.aau.se2.gwent.views.common.CardView;

public class CardRowHelper {
  public static void setCardsAlpha(ArrayList<CardView> cardViews, float alpha) {
    float bound = Math.max(0, Math.min(alpha, 1)); // only floats between 0.0 and 1.0
    for (CardView cardView : cardViews) {
      cardView.setAlpha(bound);
    }
  }

  public static void setCardsOnClickListener(
      ArrayList<CardView> cardViews, View.OnClickListener listener) {
    for (CardView cardView : cardViews) {
      cardView.setOnClickListener(listener);
    }
  }

  public static void setCardsVisibilityForPlaceholders(
      ArrayList<CardView> cardViews, int visibility) {
    for (CardView cardView : cardViews) {
      if (cardView.getCardId() != null) {
        cardView.setVisibility(View.VISIBLE);
        continue;
      }

      cardView.setVisibility(visibility);
    }
  }

  public static void removeCardViews(CardareaBinding binding) {
    binding.getRoot().removeAllViews();
  }

  public static ArrayList<CardView> getCardsFromLayout(CardareaBinding layout) {
    int childCount = layout.getRoot().getChildCount();
    ArrayList<CardView> cards = new ArrayList<>();
    for (int i = 0; i < childCount; i++) {
      CardView cardView = (CardView) layout.getRoot().getChildAt(i);
      if (cardView == null) continue;
      cards.add(cardView);
    }
    return cards;
  }
}
