package net.rmelick.hanabi.bot.live.connector;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveGamesManager {
    private final Map<Long, ActiveGame> _activeGames = new ConcurrentHashMap<>();

    public void joinGame(Long gameID, String password) throws IOException, InterruptedException {
        ActiveGame activeGame = new ActiveGame(gameID, password);
        _activeGames.put(gameID, activeGame);
        activeGame.init();
    }
}
