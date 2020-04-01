package net.rmelick.hanabi.live.bot.api.lobby;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class GameStateSummary {
  @JsonProperty("game_id")
  public String gameId;
  public GameStatus status;
  @JsonProperty("num_players")
  public int numPlayers;
  public List<PlayerInfo> players;
}
