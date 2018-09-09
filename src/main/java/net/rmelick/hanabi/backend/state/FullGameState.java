package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Hint;
import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
  private final String _gameId = UUID.randomUUID().toString();
  private final int _numPlayers;
  private int _currentPlayerIndex;
  private PlayerState _currentPlayer;

  public FullGameState(int numPlayers) {
    if (numPlayers < 2) {
      throw new IllegalArgumentException("not enough players");
    } else if (numPlayers > 5) {
      throw new IllegalArgumentException("too many players");
    }
    _numPlayers = numPlayers;

    for(int playerIndex = 0; playerIndex < _numPlayers; playerIndex++) {
      _playerStates.add(new PlayerState(playerIndex));
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

  private void checkIsTurn(String playerId) {
    if (!_currentPlayer.getId().equals(playerId)) {
      throw new IllegalStateException("Not the players turn: " + playerId);
    }
  }

  private void draw(String playerId) {
    checkIsTurn(playerId);
    unsafeDraw(playerId);
  }

  // do not check if it is allowed
  private void unsafeDraw(String playerId) {
    unsafeDrawOffTurn(playerId);
    advanceTurn();
  }

  // internal draw for specific player that does not advance the turn
  private void unsafeDrawOffTurn(String playerId) {
    Tile nextTile = _drawPileState.drawTile();
    getPlayer(playerId).receiveTile(nextTile);
  }

  public void discard(String playerId, int positionToDiscard) {
    checkIsTurn(playerId);
    unsafeDiscard(playerId, positionToDiscard);
  }

  private void unsafeDiscard(String playerId, int positionToDiscard) {
    Tile discardedTile = getPlayer(playerId).removeTile(positionToDiscard);
    _discardPileState.discard(discardedTile);
    if (_cluesRemaining.get() < MAX_CLUES) {
      _cluesRemaining.incrementAndGet();
    }
    draw(playerId);
  }

  public void play(String playerId, int positionToPlay) {
    checkIsTurn(playerId);
    unsafePlayOffTurn(playerId, positionToPlay);
    advanceTurn();
  }

  private void unsafePlayOffTurn(String playerId, int positionToPlay) {
    Tile tileToPlay = getPlayer(playerId).removeTile(positionToPlay);
    boolean successfullyPlayed = _boardState.play(tileToPlay);
    if (!successfullyPlayed) {
      _mistakesRemaining.decrementAndGet();
    }
    unsafeDrawOffTurn(playerId);
  }

  public void hint(String playerId, String recipientPlayerId, Hint hint) {
    checkIsTurn(playerId);
    checkCluesAvailable();
    applyHint(hint, getPlayer(recipientPlayerId));
    advanceTurn();
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

  private void advanceTurn() {
    _currentPlayerIndex += 1;
    if (_currentPlayerIndex >= _numPlayers) {
      _currentPlayerIndex = 0;
    }
    _currentPlayer = _playerStates.get(_currentPlayerIndex);
    // TODO notify outwards that the turn is over?
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
