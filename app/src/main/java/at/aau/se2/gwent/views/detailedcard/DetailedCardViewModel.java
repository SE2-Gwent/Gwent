package at.aau.se2.gwent.views.detailedcard;

import java.util.Arrays;
import java.util.LinkedList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gwent.data.Card;

public class DetailedCardViewModel extends ViewModel {
  private Card card;

  private MutableLiveData<DetailedCardViewModel.ViewState> currentState =
      new MutableLiveData<>(DetailedCardViewModel.ViewState.INITIAL);

  enum ViewState {
    INITIAL,
    LOADED
  }

  public DetailedCardViewModel() {
    // TODO: retrieve card (card which was clicked by the user)

    // TODO: ----REMOVE-----
    // init a sample card to test functionality
    LinkedList<Card.CardType> types =
        new LinkedList<>(Arrays.asList(Card.CardType.Human, Card.CardType.Knight));
    this.card =
        new Card(
            42,
            "Ard Feainn Crossbow Man",
            types,
            4,
            "Deploy: Damage an enemy unit by 2. Barricade: Damage a random enemy unit by 1 whenever you play a soldier. Lorem ipsum, dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "Many thought that mounted crossbowman wouldn't work. Until they saw them in action.",
            0);
    // TODO: ------REMOVE------

    currentState.setValue(ViewState.LOADED);
  }

  public MutableLiveData<DetailedCardViewModel.ViewState> getCurrentState() {
    return currentState;
  }

  public Card getCard() {
    return card;
  }
}
