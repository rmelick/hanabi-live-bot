package net.rmelick.hanabi.backend;

import net.rmelick.hanabi.backend.state.FullGameState;

/**
 *
 */
public class CompletedGame {
  private final FullGameState _gameState;

  public CompletedGame(FullGameState gameState) {
    _gameState = gameState;
  }

  public FullGameState getGameState() {
    return _gameState;
  }
}
