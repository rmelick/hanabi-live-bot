package net.rmelick.hanabi.live.bot;

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

  public static Color fromPrettyName(String prettyName) {
    for (Color color : Color.values()) {
      if (color.getPrettyName().equals(prettyName)) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color " + prettyName);
  }
}
