package at.aau.se2.gwent.views;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gwent.Board;

public class Boardviewmodel extends ViewModel {

  private MutableLiveData<Board.State> currentState = new MutableLiveData<>(Board.State.INITIAL);

  public MutableLiveData<Board.State> getCurrentState() {
    return currentState;
  }

  public void setCurrentState(MutableLiveData<Board.State> currentState) {
    this.currentState = currentState;
  }

  public ArrayList<Integer> getHandcards() {
    return new ArrayList<>(10);
  }
}
