package at.aau.se2.gamelogic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import at.aau.se2.gamelogic.state.GameState;

public interface GameStateCallback {
  void gameStateChanged(@NonNull GameState current, @Nullable GameState old);
}
