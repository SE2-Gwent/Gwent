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
    /*
    TODO:
    here we need a unique name which refers to the cardImage (large used for detailed card view)
    i would suggest to use the cardName + postfix (large) (for this we would need a small function
    which removes spaces from name and appends "large" to it afterwards
     */
        imgResourceName = "detailed_card_test_to_load";

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
}
