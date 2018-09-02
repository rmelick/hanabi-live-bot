package net.rmelick.hanabi.backend.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class Players {
  @JsonProperty("this_player")
  public Player thisPlayer;
  @JsonProperty("other_players")
  public List<Player> otherPlayers;
}
