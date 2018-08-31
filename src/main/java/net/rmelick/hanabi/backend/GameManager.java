package net.rmelick.hanabi.backend;

import net.rmelick.hanabi.backend.state.FullGameState;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages multiple active games
 */
public class GameManager {
    private final ConcurrentHashMap<String, FullGameState> _currentGames = new ConcurrentHashMap<>();

    public String createGame(int numPlayers) {
        String gameId = UUID.randomUUID().toString();
        FullGameState game = new FullGameState(numPlayers);
        _currentGames.put(gameId, game);
        return gameId;
    }

    public FullGameState getGame(String gameId) {
        return _currentGames.get(gameId);
    }
}
