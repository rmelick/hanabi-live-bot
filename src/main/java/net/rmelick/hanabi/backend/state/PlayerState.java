package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class PlayerState {
  private final String _id;
  private final String _name;
  private final int _playerIndex;
  private final List<Tile> _tiles;

  public PlayerState(int playerIndex) {
    _name = "Player " + playerIndex;
    _playerIndex = playerIndex;
    _tiles = new ArrayList<>();
    _id = UUID.randomUUID().toString();
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

  public String getName() {
    return _name;
  }

  public int getPlayerIndex() {
    return _playerIndex;
  }

  public String getId() {
    return _id;
  }

  public List<Tile> getTiles() {
    return _tiles;
  }

  @Override public String toString() {
    return "PlayerState{" +
        "_playerIndex=" + _playerIndex +
        ", _tiles=" + _tiles +
        '}';
  }
}
