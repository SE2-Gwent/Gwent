package at.aau.se2.gwent;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import at.aau.se2.gwent.views.detailedcard.CardDetails;

public class Board {
  public enum State {
    INITIAL,
    ACTIVATEHERO,
    CARDCLICKED,
    CARDPLACED,
    DONE
  }
  ArrayList<CardDetails> handcards = new ArrayList<>(10);

}
