package net.rmelick.hanabi.backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class DrawPile {
  @JsonProperty("tiles_remaining")
  public int tilesRemaining;
}
