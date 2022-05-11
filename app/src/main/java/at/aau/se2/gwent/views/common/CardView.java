package at.aau.se2.gwent.views.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import at.aau.se2.gwent.R;

public class CardView extends ViewGroup {
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

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {}

  private void init(Context context) {
    View.inflate(context, R.layout.view_card, null);
  }
}
