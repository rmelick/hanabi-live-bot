package net.rmelick.hanabi.backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class HintMove {
  @JsonProperty("player_id")
  public String playerId;
  public int rank;
  public String color;
}
