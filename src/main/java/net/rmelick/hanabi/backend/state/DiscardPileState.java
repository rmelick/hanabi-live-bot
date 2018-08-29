package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DiscardPileState {
  private final List<Tile> _tiles = new ArrayList<>();

  public List<Tile> getTiles() {
    return _tiles;
  }

  public void discard(Tile tile) {
    _tiles.add(tile);
  }

  @Override
  public String toString() {
    return "DiscardPileState{" +
        "_tiles=" + _tiles +
        '}';
  }
}
