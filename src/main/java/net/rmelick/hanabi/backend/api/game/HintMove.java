package net.rmelick.hanabi.backend.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class HintMove {
  @JsonProperty("player_id")
  public String playerId;
  public String color;
  public Integer rank;
}
