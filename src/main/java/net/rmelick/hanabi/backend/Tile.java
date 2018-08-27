package net.rmelick.hanabi.backend;

/**
 *
 */
public class Tile {
    private final Color _color;
    private final Rank _rank;
    private final String _id;

    public Tile(Color color, Rank rank, int number) {
      _color = color;
      _rank = rank;
      _id = String.format("%s-%s-%s", color, rank, number);
    }

  @Override public String toString() {
    return "Tile{" +
        _id +
        " " + _color +
        " " + _rank +
        '}';
  }
}
