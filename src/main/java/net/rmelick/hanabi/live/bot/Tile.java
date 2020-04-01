package net.rmelick.hanabi.live.bot;

/**
 *
 */
public class Tile {
    private final Color _color;
    private final Rank _rank;
    private final String _id;
    private final String _publicId;

    public Tile(Color color, Rank rank, int number) {
      _color = color;
      _rank = rank;
      _id = String.format("%s-%s-%s", color, rank, number);
      _publicId = "public-" + _id;
    }

  public Color getColor() {
    return _color;
  }

  public Rank getRank() {
    return _rank;
  }

  public String getId() {
    return _id;
  }

  public String getPublicId() {
    return _publicId;
  }

  @Override public String toString() {
    return "Tile{" +
        _id +
        " " + _color +
        " " + _rank +
        '}';
  }
}
