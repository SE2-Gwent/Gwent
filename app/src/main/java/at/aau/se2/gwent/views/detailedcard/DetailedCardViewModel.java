package at.aau.se2.gwent.views.detailedcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardType;
import at.aau.se2.gwent.Environment;

public class DetailedCardViewModel extends ViewModel {
  private CardDetails card;
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();

  private MutableLiveData<CardDetails.ViewState> currentState =
      new MutableLiveData<>(CardDetails.ViewState.INITIAL);

  private static final String TAG = DetailedCardViewModel.class.getSimpleName();

  public DetailedCardViewModel() {}

  public void setCardDetails(String cardId) {
    if (cardId == null) {
      Log.w(TAG, "Argument cardId is null!");
      return;
    }

    Card clickedCard = null;
    HashMap<String, Card> allHands = gameLogic.getGameField().getCurrentHandCards().getAllDecks();
    HashMap<String, Card> allRows = gameLogic.getGameFieldRows().getAllRows();

    if (allHands.containsKey(cardId)) {
      clickedCard = allHands.get(cardId);
    } else {
      for (Map.Entry<String, Card> entry : allRows.entrySet()) {
        Card currentCard = entry.getValue();
        if (currentCard.getFirebaseId().equals(cardId)) {
          clickedCard = currentCard;
          break;
        }
      }
    }

    if (clickedCard != null) {
      this.card =
          new CardDetails(
              clickedCard.getName(),
              convertTypesToString(clickedCard.getTypes()),
              clickedCard.getCurrentAttackPoints(),
              clickedCard.getPowerDiff(),
              clickedCard.getCardText(),
              clickedCard.getImgResourceDetail());

      currentState.setValue(CardDetails.ViewState.LOADED);
    }

    /*
    Log.i(TAG, card.getName());
    Log.i(TAG, card.getTypes());
    Log.i(TAG, String.valueOf(card.getPower()));
    Log.i(TAG, String.valueOf(card.getPowerDiff()));
    Log.i(TAG, card.getCardText());
    Log.i(TAG, card.getImgResourceName());
    */
  }

  public MutableLiveData<CardDetails.ViewState> getCurrentState() {
    return currentState;
  }

  public void setCurrentState(MutableLiveData<CardDetails.ViewState> currentState) {
    this.currentState = currentState;
  }

  public CardDetails getCard() {
    return card;
  }

  // set textView cardTypes (convert Array of enums to string)
  private String convertTypesToString(ArrayList<CardType> types) {
    String res = "";
    if (types == null) {
      return res;
    }

    for (int i = 0; i < types.size(); i++) {
      String tmp = types.get(i).toString();
      res += tmp.charAt(0) + tmp.substring(1).toLowerCase();
      if (i != types.size() - 1) {
        res += ", ";
      }
    }
    return res;
  }
}
