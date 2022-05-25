package at.aau.se2.gwent;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.app.AppCompatActivity;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.views.common.CardView;

public class MainGameActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);
  }
}
