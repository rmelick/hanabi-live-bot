package net.rmelick.hanabi.live.bot;

import net.rmelick.hanabi.live.bot.state.FullGameState;

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
