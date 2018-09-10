package net.rmelick.hanabi.backend.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class HintsForPlayer {
  @JsonProperty("color_hints")
  public List<String> colorHints;
  @JsonProperty("rank_hints")
  public List<Integer> rankHints;
}
