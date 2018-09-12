package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Hint;
import net.rmelick.hanabi.backend.PlayerInfo;
import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class FullGameState {
  private static final int MAX_CLUES = 8;
  private final AtomicLong _cluesRemaining = new AtomicLong(MAX_CLUES);
  private final AtomicLong _mistakesRemaining = new AtomicLong(3);
  private final List<PlayerState> _playerStates = new ArrayList<>();
  private final DrawPileState _drawPileState = new DrawPileState();
  private final DiscardPileState _discardPileState = new DiscardPileState();
  private final BoardState _boardState = new BoardState();
  private final String _gameId;
  private final int _numPlayers;
  private int _currentPlayerIndex;
  private PlayerState _currentPlayer;
  private boolean _gameIsCompleted;
  private String _playerToDrawLastTile;

  public FullGameState(String gameId, List<PlayerInfo> players) {
    _gameId = gameId;
    _gameIsCompleted = false;
    _playerToDrawLastTile = null;

    int numPlayers = players.size();
    if (numPlayers < 2) {
      throw new IllegalArgumentException("not enough players");
    } else if (numPlayers > 5) {
      throw new IllegalArgumentException("too many players");
    }
    _numPlayers = numPlayers;

    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      PlayerInfo player = players.get(playerIndex);
      _playerStates.add(new PlayerState(player.getId(),
          player.getName(), player.getType(), playerIndex));
    }
    _currentPlayerIndex = 0;
    _currentPlayer = _playerStates.get(_currentPlayerIndex);

    int tilesToDraw = numPlayers <= 3 ? 5 : 4;
    for(PlayerState player : _playerStates) {
      for (int i = 0; i < tilesToDraw; i++) {
        unsafeDrawOffTurn(player.getId());
      }
    }
  }

  public String getGameId() {
    return _gameId;
  }

  public PlayerState getPlayer(String playerId) {
    for (PlayerState player : _playerStates) {
      if (player.getId().equals(playerId)) {
        return player;
      }
    }
    throw new IllegalArgumentException("Could not find player " + playerId);
  }

  private void checkBeginningOfTurn(String playerId) {
    if (_gameIsCompleted) {
      throw new IllegalStateException("Game is completed");
    }
    if (!_currentPlayer.getId().equals(playerId)) {
      throw new IllegalStateException("Not the players turn: " + playerId);
    }
  }

  // do not check if it is allowed
  private void unsafeDraw(String playerId) {
    unsafeDrawOffTurn(playerId);
  }

  // internal draw for specific player that does not advance the turn
  private void unsafeDrawOffTurn(String playerId) {
    Tile nextTile = _drawPileState.drawTile();
    getPlayer(playerId).receiveTile(nextTile);
  }

  /**
   *
   */
  public void discard(String playerId, int positionToDiscard) {
    checkBeginningOfTurn(playerId);
    unsafeDiscard(playerId, positionToDiscard);
    endTurn(playerId);
  }

  private void unsafeDiscard(String playerId, int positionToDiscard) {
    Tile discardedTile = getPlayer(playerId).removeTile(positionToDiscard);
    _discardPileState.discard(discardedTile);
    if (_cluesRemaining.get() < MAX_CLUES) {
      _cluesRemaining.incrementAndGet();
    }
    unsafeDraw(playerId);
  }

  /**
   *
   */
  public void play(String playerId, int positionToPlay) {
    checkBeginningOfTurn(playerId);
    unsafePlayOffTurn(playerId, positionToPlay);
    endTurn(playerId);
  }

  private void unsafePlayOffTurn(String playerId, int positionToPlay) {
    Tile tileToPlay = getPlayer(playerId).removeTile(positionToPlay);
    boolean successfullyPlayed = _boardState.play(tileToPlay);
    if (!successfullyPlayed) {
      _mistakesRemaining.decrementAndGet();
      _discardPileState.discard(tileToPlay);
    }
    unsafeDrawOffTurn(playerId);
  }

  /**
   *
   */
  public void hint(String playerId, String recipientPlayerId, Hint hint) {
    checkBeginningOfTurn(playerId);
    checkCluesAvailable();
    applyHint(hint, getPlayer(recipientPlayerId));
    _cluesRemaining.decrementAndGet();
    endTurn(playerId);
  }

  private void checkCluesAvailable() {
    if (_cluesRemaining.get() <= 0) {
      throw new IllegalStateException("No clues remaining");
    }
  }

  private void applyHint(Hint hint, PlayerState player) {
    for (TileInHand tileInHand : player.getTilesInHand()) {
      if (Hint.HintType.COLOR.equals(hint.getHintType()) && hint.getColorHint().equals(tileInHand.getTile().getColor())) {
        tileInHand.addMatchingHint(hint);
      }
      else if (Hint.HintType.RANK.equals(hint.getHintType()) && hint.getRankHint().equals(tileInHand.getTile().getRank())) {
        tileInHand.addMatchingHint(hint);
      } else {
        tileInHand.addNonMatchingHint(hint);
      }
    }
  }

  private void endTurn(String playerId) {
    _currentPlayerIndex += 1;
    if (_currentPlayerIndex >= _numPlayers) {
      _currentPlayerIndex = 0;
    }
    _currentPlayer = _playerStates.get(_currentPlayerIndex);
    checkForGameOver(playerId);
  }

  private void checkForGameOver(String playerId) {
    if (_mistakesRemaining.get() <= 0) {
      _gameIsCompleted = true;
    }
    if (_drawPileState.tilesRemaining() == 0) {
      if (_playerToDrawLastTile == null) {
        // track who drew the last tile
        _playerToDrawLastTile = playerId;
      } else if (_playerToDrawLastTile.equals(playerId)) {
        // end the game after they take another turn
        _gameIsCompleted = true;
      }
    }
  }

  public AtomicLong getCluesRemaining() {
    return _cluesRemaining;
  }

  public AtomicLong getMistakesRemaining() {
    return _mistakesRemaining;
  }

  public PlayerState getCurrentPlayerState() {
    return _playerStates.get(_currentPlayerIndex);
  }

  public List<PlayerState> getPlayerStates() {
    return _playerStates;
  }

  public DrawPileState getDrawPileState() {
    return _drawPileState;
  }

  public DiscardPileState getDiscardPileState() {
    return _discardPileState;
  }

  public BoardState getBoardState() {
    return _boardState;
  }

  public boolean isGameIsCompleted() {
    return _gameIsCompleted;
  }

  @Override
  public String toString() {
    StringBuilder gameState = new StringBuilder();
    gameState.append("Clues Remaining: " + _cluesRemaining + "\n");
    gameState.append("Mistakes Remaining: " + _mistakesRemaining + "\n");
    gameState.append("Draw Tiles Remaining: " + _drawPileState.tilesRemaining() + "\n");
    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      gameState.append(_playerStates.get(playerIndex));
      gameState.append("\n");
    }
    return gameState.toString();
  }
}
