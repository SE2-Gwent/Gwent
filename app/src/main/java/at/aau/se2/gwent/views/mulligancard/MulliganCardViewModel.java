package at.aau.se2.gwent.views.mulligancard;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gamelogic.GameLogic;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.Environment;

public class MulliganCardViewModel extends ViewModel {
  private final GameLogic gameLogic = Environment.getSharedInstance().getGameLogic();
  // public Result<Object, Object> state;

  private MutableLiveData<ArrayList<Card>> state = new MutableLiveData<>();

  public MulliganCardViewModel() {
    state.setValue(gameLogic.getCardsToMulligan());
  }

  public MutableLiveData<ArrayList<Card>> getCurrentState() {
    return state;
  }

  // did click cancel()



}
// did click card(id)



