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

  public void didClickCancel() {
    gameLogic.abortMulliganCards();
  }

  public void didClickCard(int id) {
    // Im State Card mit Id finden
    // State aktualisieren, damit neue Card angezeigt wird.
    Card card = gameLogic.mulliganCard(id);
    ArrayList<Card> cardList = state.getValue();

    int index = -1;
    for (int i = 0; cardList.size() > i; i++) {
      if (cardList.get(i).getId() == id) {
        index = i;
        break;
      }
    }

    if (index == -1) {
      return;
    }

    cardList.remove(index);

    cardList.add(index, card);

    state.setValue(cardList);
  }
}
