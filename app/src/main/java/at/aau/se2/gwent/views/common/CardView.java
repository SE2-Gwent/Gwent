package at.aau.se2.gwent.views.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import at.aau.se2.gwent.R;

public class CardView extends FrameLayout {
  private View view;
  private androidx.cardview.widget.CardView cardView;
  private ImageView cardImageView;
  private TextView pointTextView;
  private TextView nameTextView;
  private boolean isSelected = false;
  private final Drawable borderDrawable = getResources().getDrawable(R.drawable.cardview_border);
  private String cardId;

  public CardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CardView(Context context, AttributeSet attrs, int defStyleattr) {
    super(context, attrs);
    init(context);
  }

  public CardView(Context context, AttributeSet attrs, int defStyleattr, int defStyleRes) {
    super(context, attrs);
    init(context);
  }

  public void setupWithCard(
      String cardId, int attackPoints, String name, @DrawableRes int cardImage) {
    this.cardId = cardId;
    pointTextView.setText(String.valueOf(attackPoints));
    nameTextView.setText(name);
    cardImageView.setImageDrawable(getResources().getDrawable(cardImage));
    setAlpha(1.0F);
  }

  public void showAsPlaceholder() {
    pointTextView.setText(null);
    nameTextView.setText(null);
    cardImageView.setImageDrawable(getResources().getDrawable(R.drawable.card_background));
    setAlpha(0.4F);
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
    updateUI();
  }

  private void init(Context context) {
    view = View.inflate(context, R.layout.view_card, null);
    addView(view);

    cardView = view.findViewById(R.id.cardView);
    cardImageView = view.findViewById(R.id.imageViewCardBackground);
    nameTextView = view.findViewById(R.id.textViewName);
    pointTextView = view.findViewById(R.id.textViewPoints);

    ConstraintLayout.LayoutParams params =
        new ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    params.leftMargin = 5;
    params.rightMargin = 5;
    setLayoutParams(params);

    showAsPlaceholder();

    updateUI(true);
  }

  private void updateUI() {
    updateUI(false);
  }

  private void updateUI(boolean disableAnimation) {
    cardView.setForeground(isSelected ? borderDrawable : null); // ? = Tenary Operator

    if (disableAnimation) return;

    Animation animation =
        new ScaleAnimation(
            isSelected ? 1.0f : 1.2f,
            isSelected ? 1.2f : 1.0f,
            isSelected ? 1.0f : 1.2f,
            isSelected ? 1.2f : 1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f);

    animation.setFillAfter(true);
    animation.setDuration(500);
    startAnimation(animation);
  }

  public String getCardId() {
    return cardId;
  }

  public ImageView getCardImageView() {
    return cardImageView;
  }
}
