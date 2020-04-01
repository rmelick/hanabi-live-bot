package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class Player {
  @JsonProperty("player_index")
  public int playerIndex;
  @JsonProperty("name")
  public String name;
  @JsonProperty("is_current_player")
  public boolean isCurrentPlayer;
  @JsonProperty("tiles")
  public List<Tile> tiles;
  public String id;
}
