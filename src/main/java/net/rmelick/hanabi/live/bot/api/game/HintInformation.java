package net.rmelick.hanabi.live.bot.api.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class HintInformation {
  @JsonProperty("possible_colors")
  public List<String> possibleColors;
  @JsonProperty("possible_ranks")
  public List<Integer> possibleRanks;
  @JsonProperty("positive_hints_given")
  public List<HintMove> positiveHintsGiven;
  @JsonProperty("negative_hints_given")
  public List<HintMove> negativeHintsGiven;
}
