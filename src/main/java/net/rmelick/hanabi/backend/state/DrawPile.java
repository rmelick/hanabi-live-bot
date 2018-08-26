package net.rmelick.hanabi.backend.state;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DrawPile {
  private final List<Tile> _tiles = newRandomList();

  private static List<Tile> newRandomList() {
    List<Tile> list = new ArrayList<>(60);
    for (Color color : Color.values()) {
      // 3 ones
      list.add(new Tile(color, Rank.ONE));
      list.add(new Tile(color, Rank.ONE));
      list.add(new Tile(color, Rank.ONE));
      // 2 twos
      list.add(new Tile(color, Rank.TWO));
      list.add(new Tile(color, Rank.TWO));
      // 2 threes
      list.add(new Tile(color, Rank.THREE));
      list.add(new Tile(color, Rank.THREE));
      // 2 fours
      list.add(new Tile(color, Rank.FOUR));
      // 2 fives
      list.add(new Tile(color, Rank.FIVE));
    }
    Collections.shuffle(list);
    return list;
  }

  public int tilesRemaining() {
    return _tiles.size();
  }

  public Tile drawTile() {
    return _tiles.remove(0);
  }

}
