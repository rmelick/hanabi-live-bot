package net.rmelick.hanabi.backend;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class GameWaitingToBegin {
  private final String _gameId = UUID.randomUUID().toString();
  private final List<PlayerInfo> _players = new ArrayList<>();

  public void addPlayer(PlayerInfo playerInfo) {
    _players.add(playerInfo);
  }

  public String getGameId() {
    return _gameId;
  }

  public List<PlayerInfo> getPlayers() {
    return _players;
  }
}
