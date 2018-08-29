package net.rmelick.hanabi.backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class ViewableGameState {
  public List<Player> players;
  @JsonProperty("draw_pile")
  public DrawPile drawPile;
  @JsonProperty("discard_pile")
  public DiscardPile discardPile;
  public Board board;
}
