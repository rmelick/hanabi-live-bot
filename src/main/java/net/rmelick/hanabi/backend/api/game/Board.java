package net.rmelick.hanabi.backend.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Board {
  @JsonProperty("played_tiles")
  public Map<String, List<Tile>> playedTiles;
}
