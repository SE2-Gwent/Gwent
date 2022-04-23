package at.aau.se2.gwent.views.detailedcard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailedCardViewModel extends ViewModel {
    // TODO: add var. card
    // TODO: add var. img (which contains the artwork of the card)

    private MutableLiveData<DetailedCardViewModel.ViewState> currentState = new MutableLiveData<>(DetailedCardViewModel.ViewState.INITIAL);

    public MutableLiveData<DetailedCardViewModel.ViewState> getCurrentState() {
        return currentState;
    }

    enum ViewState {
        INITIAL,
        LOADED
    }

    public DetailedCardViewModel() {
        // TODO: init current card
        // TODO: fetch img. by id of current card

        currentState.setValue(ViewState.LOADED);
    }
}
