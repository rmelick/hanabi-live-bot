package net.rmelick.hanabi.backend;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 *
 */
public class GameWaitingToBegin {
  private final String _gameId = UUID.randomUUID().toString();
  private final Set<PlayerInfo> _players = new TreeSet<>();

  public void addPlayer(PlayerInfo playerInfo) {
    _players.add(playerInfo);
  }

  public String getGameId() {
    return _gameId;
  }

  public List<PlayerInfo> getPlayers() {
    return new ArrayList<>(_players);
  }
}
