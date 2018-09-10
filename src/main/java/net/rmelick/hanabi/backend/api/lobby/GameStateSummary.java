package net.rmelick.hanabi.backend.api.lobby;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class GameStateSummary {
  @JsonProperty("game_id")
  public String gameId;
  public GameStatus status;
  @JsonProperty("num_players")
  public int numPlayers;
}
