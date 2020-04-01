package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Tile {
  @JsonProperty("public_id")
  public String publicId;
  @JsonProperty("id")
  public String id;
  @JsonProperty("color")
  public String color;
  @JsonProperty("rank")
  public int rank;
  @JsonProperty("hint_information")
  public HintInformation hintInformation;
}
