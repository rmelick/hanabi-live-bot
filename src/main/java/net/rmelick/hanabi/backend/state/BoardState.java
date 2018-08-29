package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BoardState {
  private final Map<String, List<Tile>> _playedTiles;

  public BoardState() {
    _playedTiles = new HashMap<>();
    for (Color color : Color.values()) {
      _playedTiles.put(color.getPrettyName(), new ArrayList<>(Rank.values().length));
    }
  }

  public Map<String, List<Tile>> getPlayedTiles() {
    return _playedTiles;
  }
}
