package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class GameState {
  private final AtomicLong _cluesRemaining = new AtomicLong(8);
  private final AtomicLong _mistakesRemaining = new AtomicLong(3);
  private final List<PlayerState> _playerStates = new ArrayList<>();
  private final DrawPile _drawPile = new DrawPile();
  private final int _numPlayers;
  private int _currentPlayer;

  public GameState(int numPlayers) {
    if (numPlayers < 2) {
      throw new IllegalArgumentException("not enough players");
    } else if (numPlayers > 5) {
      throw new IllegalArgumentException("too many players");
    }
    _numPlayers = numPlayers;
    _currentPlayer = 0;

    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      _playerStates.add(new PlayerState(playerIndex));
    }

    int tilesToDraw = numPlayers <= 3 ? 5 : 4;
    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      for (int i = 0; i < tilesToDraw; i++) {
        draw(playerIndex);
      }
    }

  }

  public void draw(int playerIndex) {
    Tile nextTile = _drawPile.drawTile();
    _playerStates.get(playerIndex).receiveTile(nextTile);
  }

  @Override
  public String toString() {
    StringBuilder gameState = new StringBuilder();
    gameState.append("Clues Remaining: " + _cluesRemaining + "\n");
    gameState.append("Mistakes Remaining: " + _mistakesRemaining + "\n");
    gameState.append("Draw Tiles Remaining: " + _drawPile.tilesRemaining() + "\n");
    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      gameState.append(_playerStates.get(playerIndex));
      gameState.append("\n");
    }
    return gameState.toString();
  }
}
