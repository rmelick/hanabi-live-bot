package net.rmelick.hanabi.backend;

/**
 *
 */
public class Clue {
  public enum ClueType {
    COLOR,
    RANK
  }

  private final ClueType _clueType;
  private final Color _colorClue;
  private final Rank _rankClue;

  public Clue(ClueType clueType, Color colorClue, Rank rankClue) {
    _clueType = clueType;
    _colorClue = colorClue;
    _rankClue = rankClue;
  }
}
