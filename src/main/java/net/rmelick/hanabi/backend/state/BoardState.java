package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BoardState {
  private final Map<Color, List<Tile>> _playedTiles;

  public BoardState() {
    _playedTiles = new HashMap<>();
    for (Color color : Color.values()) {
      _playedTiles.put(color, new ArrayList<>(Rank.values().length));
    }
  }

  public boolean play(Tile tile) {
    if (alreadyPlayed(tile)) {
      System.out.println(tile + " has already been played");
      return false;
    }
    else if (!isNextInColor(tile)) {
      System.out.println(tile + " is not the next tile in its color");
      return false;
    } else {
      _playedTiles.get(tile.getColor()).add(tile);
      return true;
    }
  }

  private boolean alreadyPlayed(Tile tile) {
    return colorContainsRank(tile.getColor(), tile.getRank());
  }

  private boolean isNextInColor(Tile tile) {
    Color color = tile.getColor();
    switch (tile.getRank()) {
    case ONE:
      return _playedTiles.get(color).isEmpty();
    case TWO:
      return colorContainsRank(color, Rank.ONE);
    case THREE:
      return colorContainsRank(color, Rank.TWO);
    case FOUR:
      return colorContainsRank(color, Rank.THREE);
    case FIVE:
      return colorContainsRank(color, Rank.FOUR);
    default:
      throw new IllegalStateException("Unknown Rank");
    }
  }

  private boolean colorContainsRank(Color color, Rank rank) {
    for (Tile playedTile : _playedTiles.get(color)) {
      if (playedTile.getRank().equals(rank)) {
        return true;
      }
    }
    return false;
  }

  public Map<Color, List<Tile>> getPlayedTiles() {
    return _playedTiles;
  }
}
