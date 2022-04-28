package at.aau.se2.gwent.views.detailedcard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailedCardViewModel extends ViewModel {
  private String name;
  private String type;
  private String text;
  private String flavorText;
  private String power;
  private int powerDiff;

  private String imgResourceName;

  private MutableLiveData<DetailedCardViewModel.ViewState> currentState =
      new MutableLiveData<>(DetailedCardViewModel.ViewState.INITIAL);

  enum ViewState {
    INITIAL,
    LOADED
  }

  public DetailedCardViewModel() {
    /*
    TODO:
    1) retrieve id of the card clicked by the user
    2) parse json-card according to id (set attributes: cardName, cardTypes, etc.)
     */

    // TODO: Remove dummy-attributes
    this.name = "Ard Feainn Crossbow Man";
    this.type = "Human, Knight";
    this.text =
        "Deploy: Damage an enemy unit by 2. Barricade: Damage a random enemy unit by 1 whenever you play a soldier. Lorem ipsum, dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    this.flavorText =
        "Many thought that mounted crossbowman wouldn't work. Until they saw them in action.";
    this.power = "3";
    this.powerDiff = 1;

    imgResourceName = "detailed_card_test_to_load";
    // End

    currentState.setValue(ViewState.LOADED);
  }

  public MutableLiveData<DetailedCardViewModel.ViewState> getCurrentState() {
    return currentState;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  public String getFlavorText() {
    return flavorText;
  }

  public String getPower() {
    return power;
  }

  public int getPowerDiff() {
    return powerDiff;
  }

  public String getImgResourceName() {
    return imgResourceName;
  }
}
