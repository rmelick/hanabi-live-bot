package net.rmelick.hanabi.backend.api.game;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class AvailableMoves {
  public List<DiscardMove> discards;
  public List<PlayMove> plays;
  public Map<String, HintsForPlayer> hints;
}
