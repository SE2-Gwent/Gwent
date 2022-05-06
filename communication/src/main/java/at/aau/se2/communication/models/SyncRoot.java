package at.aau.se2.communication.models;

import java.util.ArrayList;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.Player;

public class Game {
  ArrayList<Player> player;

  @Keep
  public Game() {}

  public Game(ArrayList<Player> player) {
    this.player = player;
  }

  public ArrayList<Player> getPlayer() {
    return player;
  }
}
