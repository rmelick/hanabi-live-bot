package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class ViewableGameState {
  @JsonProperty("game_id")
  public String gameId;
  public Players players;
  @JsonProperty("draw_pile")
  public DrawPile drawPile;
  @JsonProperty("discard_pile")
  public DiscardPile discardPile;
  public Board board;
  @JsonProperty("clues_remaining")
  public int cluesRemaining;
  @JsonProperty("mistakes_remaining")
  public int mistakesRemaining;
  @JsonProperty("available_moves")
  public AvailableMoves availableMoves;
  @JsonProperty("is_completed")
  public boolean isCompleted;
}
