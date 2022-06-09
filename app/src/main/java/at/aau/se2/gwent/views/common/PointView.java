package at.aau.se2.gwent.views.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import at.aau.se2.gwent.R;

public class PointView extends FrameLayout {
  private View view;

  public PointView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PointView(Context context, AttributeSet attrs, int defStyleattr) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    view = View.inflate(context, R.layout.view_point, null);
    addView(view);

    updateUI();
  }

  private void updateUI() {}
}
