package net.rmelick.hanabi.bot.live.connector;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveGamesManager {
    private final Map<Long, ActiveGame> _activeGames = new ConcurrentHashMap<>();

    public void joinGame(Long gameID, String password) throws IOException, InterruptedException {
        HanabiPlayerClient playerClient = new HanabiPlayerClient(gameID, password);
        HanabiSpectatorClient observerClient = new HanabiSpectatorClient(gameID);
        ActiveGame activeGame = new ActiveGame(playerClient, observerClient);
        _activeGames.put(gameID, activeGame);
        activeGame.init();
    }
}
