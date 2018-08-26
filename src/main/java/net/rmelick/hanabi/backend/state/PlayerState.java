package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PlayerState {
  private final int _playerIndex;
  private final List<Tile> _tiles;

  public PlayerState(int playerIndex) {
    _playerIndex = playerIndex;
    _tiles = new ArrayList<>();
  }

  /**
   * how the player receives the tile they drew
   * @param tile
   */
  public void receiveTile(Tile tile) {
    _tiles.add(tile);
  }

  public Tile removeTile(int tilePosition) {
    return _tiles.remove(tilePosition);
  }

  @Override public String toString() {
    return "PlayerState{" +
        "_playerIndex=" + _playerIndex +
        ", _tiles=" + _tiles +
        '}';
  }
}
