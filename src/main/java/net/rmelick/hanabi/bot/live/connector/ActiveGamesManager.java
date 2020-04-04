package net.rmelick.hanabi.bot.live.connector;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveGamesManager {
    private final Map<Long, HanabiGameClient> _activeGames = new ConcurrentHashMap<>();

    public void joinGame(Long gameID, String password) throws IOException, InterruptedException {
        HanabiGameClient gameClient = new HanabiGameClient(gameID, password);
        _activeGames.put(gameID, gameClient);
        gameClient.init();
    }
}
