package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Board {
  @JsonProperty("played_tiles")
  public Map<String, List<Tile>> playedTiles;
  @JsonProperty("scored_points")
  public int scoredPoints;
}
