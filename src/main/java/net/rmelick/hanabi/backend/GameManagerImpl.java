package net.rmelick.hanabi.backend;

import net.rmelick.hanabi.backend.api.GameManager;
import net.rmelick.hanabi.backend.state.GameState;

/**
 *
 */
public class GameManagerImpl implements GameManager {
  private GameState _currentGameState;

  @Override
  public void startNewGame(int numPlayers) {
    _currentGameState = new GameState(numPlayers);
  }

  @Override
  public void currentPlayerGivesClue(int toPlayer, Clue clue) {

  }

  @Override
  public void currentPlayerPlays(int positionToPlay) {

  }

  @Override
  public void currentPlayerDiscards(int positionToDiscard) {

  }

  @Override
  public String getDebugGameState() {
    return _currentGameState.toString();
  }
}
