package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DiscardPile {
  private final List<Tile> _tiles = new ArrayList<>();

  @Override
  public String toString() {
    return "DiscardPile{" +
        "_tiles=" + _tiles +
        '}';
  }
}
