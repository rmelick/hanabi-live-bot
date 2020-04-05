package net.rmelick.hanabi.bot.live.connector;

import net.rmelick.hanabi.bot.ieee.LiveGameRunner;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.TableStart;

import java.io.IOException;

public class ActiveGame {
    private final HanabiPlayerClient _playerClient;
    private final HanabiSpectatorClient _spectatorClient;
    private final LiveGameRunner _liveGameRunner;

    public ActiveGame(Long gameID, String password) {
        _liveGameRunner = new LiveGameRunner();
        HanabiPlayerClient playerClient = new HanabiPlayerClient(gameID, password, _liveGameRunner);
        HanabiSpectatorClient spectatorClient = new HanabiSpectatorClient(gameID, _liveGameRunner);
        _playerClient = playerClient;
        _spectatorClient = spectatorClient;
    }

    public void init() throws IOException, InterruptedException {
        _spectatorClient.init();
        _playerClient.init();
    }

}
