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
import at.aau.se2.gwent.R;

public class CardView extends FrameLayout {
  private View view;
  private androidx.cardview.widget.CardView cardView;
  private ImageView cardImageView;
  private TextView pointTextView;
  private TextView nameTextView;
  private boolean isSelected = false;
  private Drawable borderDrawable = getResources().getDrawable(R.drawable.cardview_border);

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

  public void setupWithCard(int attackPoints, String name, @DrawableRes int cardImage) {
    pointTextView.setText(String.valueOf(attackPoints));
    nameTextView.setText(name);
    cardImageView.setImageDrawable(getResources().getDrawable(cardImage));
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

    setOnClickListener(
        view -> {
          setSelected(!isSelected);
        });

    updateUI();
  }

  private void updateUI() {
    cardView.setForeground(isSelected ? borderDrawable : null); // ? = Tenary Operator

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
}
