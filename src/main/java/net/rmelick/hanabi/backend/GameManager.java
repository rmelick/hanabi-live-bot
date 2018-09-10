package net.rmelick.hanabi.backend;

import net.rmelick.hanabi.backend.state.FullGameState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages multiple active games
 */
public class GameManager {
    private final ConcurrentHashMap<String, GameWaitingToBegin> _gamesWaitingToBegin = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, FullGameState> _inProgressGames = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CompletedGame> _completedGames = new ConcurrentHashMap<>();

    public void createGame() {
        GameWaitingToBegin game = new GameWaitingToBegin();
        _gamesWaitingToBegin.put(game.getGameId(), game);
    }

    public void startGame(String gameId) {
        GameWaitingToBegin pendingGame = _gamesWaitingToBegin.remove(gameId);
        FullGameState newGame = new FullGameState(pendingGame.getGameId(), pendingGame.getPlayers());
        _inProgressGames.put(gameId, newGame);
    }

    public void completeGame(String gameId) {
        FullGameState game = _inProgressGames.remove(gameId);
        _completedGames.put(gameId, new CompletedGame(game));
    }

    public Map<String, GameWaitingToBegin> getGamesWaitingToBegin() {
        return _gamesWaitingToBegin;
    }

    public Map<String, FullGameState> getInProgressGames() {
        return _inProgressGames;
    }

    public Map<String, CompletedGame> getCompletedGames() {
        return _completedGames;
    }

    public GameWaitingToBegin getGameWaitingToBegin(String gameId) {
        return _gamesWaitingToBegin.get(gameId);
    }

    public FullGameState getInProgressGame(String gameId) {
        return _inProgressGames.get(gameId);
    }


}
