package net.rmelick.hanabi.live.bot.state;

import net.rmelick.hanabi.live.bot.Hint;
import net.rmelick.hanabi.live.bot.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TileInHand {
  private final Tile _tile;
  private final List<Hint> _matchingHints;
  private final List<Hint> _nonMatchingHints;

  public TileInHand(Tile tile) {
    _tile = tile;
    _matchingHints = new ArrayList<>(0);
    _nonMatchingHints = new ArrayList<>(0);
  }

  public Tile getTile() {
    return _tile;
  }

  public void addMatchingHint(Hint hint) {
    _matchingHints.add(hint);
  }

  public void addNonMatchingHint(Hint hint) {
    _nonMatchingHints.add(hint);
  }

  public List<Hint> getMatchingHints() {
    return _matchingHints;
  }

  public List<Hint> getNonMatchingHints() {
    return _nonMatchingHints;
  }
}
