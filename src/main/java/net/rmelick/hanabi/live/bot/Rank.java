package net.rmelick.hanabi.live.bot;

/**
 *
 */
public enum Rank {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5);

  private final int _value;

  Rank(int value) {
    _value = value;
  }

  public int getValue() {
    return _value;
  }

  @Override
  public String toString() {
    return String.valueOf(_value);
  }

  public static Rank fromInt(int value) {
    for (Rank rank : Rank.values()) {
      if (rank.getValue() == value) {
        return rank;
      }
    }
    throw new IllegalArgumentException("Illegal rank value " + value);
  }
}
