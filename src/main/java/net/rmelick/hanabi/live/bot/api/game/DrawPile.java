package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class DrawPile {
  @JsonProperty("tiles_remaining")
  public int tilesRemaining;
}
