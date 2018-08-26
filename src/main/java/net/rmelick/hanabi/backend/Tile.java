package net.rmelick.hanabi.backend;

/**
 *
 */
public class Tile {
    private final Color _color;
    private final Rank _rank;

    public Tile(Color color, Rank rank) {
      _color = color;
      _rank = rank;
    }

  @Override public String toString() {
    return "Tile{" +
          _color +
        " " + _rank +
        '}';
  }
}
