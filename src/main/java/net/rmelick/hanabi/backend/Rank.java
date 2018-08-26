package net.rmelick.hanabi.backend;

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

  @Override public String toString() {
    return String.valueOf(_value);
  }
}
