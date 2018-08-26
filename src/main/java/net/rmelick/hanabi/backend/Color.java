package net.rmelick.hanabi.backend;

/**
 *
 */
public enum Color {
  RED("red"),
  WHITE("white"),
  BLUE("blue"),
  GREEN("green"),
  YELLOW("yellow"),
  RAINBOW("rainbow");

  private final String _prettyName;

  Color(String prettyName) {
    _prettyName = prettyName;
  }

  public String getPrettyName() {
    return _prettyName;
  }

  @Override public String toString() {
    return getPrettyName();
  }
}
