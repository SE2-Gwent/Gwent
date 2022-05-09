package at.aau.se2.gwent.views.detailedcard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.models.Card;

public class DetailedCardViewModel extends ViewModel {
  private Card card;
  private String imgResourceName;

  private MutableLiveData<DetailedCardViewModel.ViewState> currentState =
      new MutableLiveData<>(DetailedCardViewModel.ViewState.INITIAL);

  enum ViewState {
    INITIAL,
    LOADED
  }

  public DetailedCardViewModel() {

    // TODO: remove dummy card
    int id = 0;
    String name = "Ard Feainn Crossbow Man";
    int power = 3;
    int powerDiff = 0;
    String cardText =
        "Deploy: Damage an enemy unit by 2. Barricade: Damage a random enemy unit by 1 whenever you play a soldier.";
    Card testCard = new Card(id, name, power, powerDiff, cardText, null);
    this.card = testCard;

    setImgResourceName();

    currentState.setValue(ViewState.LOADED);
  }

  public MutableLiveData<DetailedCardViewModel.ViewState> getCurrentState() {
    return currentState;
  }

  public Card getCard() {
    return card;
  }

  public String getImgResourceName() {
    return imgResourceName;
  }

  /*
  this function is used to set the resource name of the image to load
  we reference resources according to detailed-card-view by taking the name of the card
  first we replace all spaces by an underscore
  second we convert the string to lowercase
  third we append '_detail to it'

  for example:
  Ard Feainn Crossbow Man -> ard_feainn_crossbow_man_detail
   */
  private void setImgResourceName() {
    String imageResourceName = "";

    imageResourceName += card.getName().replaceAll(" ", "_").toLowerCase();
    imageResourceName += "_detail";

    this.imgResourceName = imageResourceName;
  }
}
