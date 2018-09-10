package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PlayerState {
  private final String _id;
  private final String _name;
  private final int _playerIndex;
  private final List<TileInHand> _tiles;

  public PlayerState(String playerId, String playerName, int playerIndex) {
    _name = playerName;
    _playerIndex = playerIndex;
    _tiles = new ArrayList<>();
    _id = playerId;
  }

  /**
   * how the player receives the tile they drew
   * @param tile
   */
  public void receiveTile(Tile tile) {
    _tiles.add(new TileInHand(tile));
  }

  public Tile removeTile(int tilePosition) {
    return _tiles.remove(tilePosition).getTile();
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

  public List<TileInHand> getTilesInHand() {
    return _tiles;
  }

  @Override public String toString() {
    return "PlayerState{" +
        "_playerIndex=" + _playerIndex +
        ", _tiles=" + _tiles +
        '}';
  }
}
