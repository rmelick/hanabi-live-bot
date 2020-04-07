package net.rmelick.hanabi.bot.live.connector;

import net.rmelick.hanabi.bot.ieee.LiveGameRunner;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ActiveGame {
    private static final AtomicInteger BOT_COUNTER = new AtomicInteger(0);

    private final HanabiPlayerClient _playerClient;
    private final HanabiSpectatorClient _spectatorClient;
    private final LiveGameRunner _liveGameRunner;

    public ActiveGame(Long gameID, String gamePassword, String botAgentName) {
        int id = BOT_COUNTER.getAndIncrement(); // TODO getAndIncrement
        String clientUsername = "rolls-bot-g" + id;
        String spectatorUsername = "rolls-obs-g" + id;
        _liveGameRunner = new LiveGameRunner(clientUsername, botAgentName);
        _playerClient = new HanabiPlayerClient(clientUsername, "iamabot", gameID, gamePassword, _liveGameRunner);
        _spectatorClient = new HanabiSpectatorClient(spectatorUsername, "iamabot", gameID, _liveGameRunner);
    }

    public void init() throws IOException, InterruptedException {
        _spectatorClient.init();
        _playerClient.init();
    }

}
