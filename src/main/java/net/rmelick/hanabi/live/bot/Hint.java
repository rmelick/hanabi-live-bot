package net.rmelick.hanabi.live.bot;

/**
 *
 */
public class Hint {
  public enum HintType {
    COLOR,
    RANK
  }

  private final HintType _hintType;
  private final Color _colorHint;
  private final Rank _rankHint;

  public Hint(HintType hintType, Color colorHint, Rank rankHint) {
    _hintType = hintType;
    _colorHint = colorHint;
    _rankHint = rankHint;
  }

  public HintType getHintType() {
    return _hintType;
  }

  public Color getColorHint() {
    return _colorHint;
  }

  public Rank getRankHint() {
    return _rankHint;
  }
}
