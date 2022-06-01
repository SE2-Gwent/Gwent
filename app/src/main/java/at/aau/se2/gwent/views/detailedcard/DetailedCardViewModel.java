package at.aau.se2.gwent.views.detailedcard;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardType;

public class DetailedCardViewModel extends ViewModel {
  private CardDetails card;

  private MutableLiveData<CardDetails.ViewState> currentState =
      new MutableLiveData<>(CardDetails.ViewState.INITIAL);

  public DetailedCardViewModel() {

    // -- START REMOVE -- (this will be replaced with a pointer to the clicked card)
    String name = "Ard Feainn Crossbow Man";
    ArrayList<CardType> types = new ArrayList<>(Arrays.asList(CardType.ELF, CardType.HUMAN));
    int power = 3;
    int powerDiff = 0;
    String cardText =
        "Deploy: Damage an enemy unit by 2. Barricade: Damage a random enemy unit by 1 whenever you play a soldier.";
    Card testCard =
        new Card(
            0,
            name,
            new ArrayList<>(Arrays.asList(CardType.ELF, CardType.HUMAN)),
            power,
            powerDiff,
            cardText,
            null,
            null,
            "rm_ard_feainn_crossbow_man_basic",
            "rm_ard_feainn_crossbow_man_detail");
    // -- END REMOVE --

    this.card =
        new CardDetails(
            testCard.getName(),
            convertTypesToString(testCard.getTypes()),
            testCard.getPower(),
            testCard.getPowerDiff(),
            testCard.getCardText(),
            testCard.getImgResourceDetail());
    currentState.setValue(CardDetails.ViewState.LOADED);
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
