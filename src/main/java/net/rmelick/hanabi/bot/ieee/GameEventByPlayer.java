package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.events.GameEvent;

public class GameEventByPlayer {
    private final GameEvent _gameEvent;
    private final int playerID;

    public GameEventByPlayer(GameEvent gameEvent, int playerID) {
        _gameEvent = gameEvent;
        this.playerID = playerID;
    }

    public GameEvent getGameEvent() {
        return _gameEvent;
    }

    public int getPlayerID() {
        return playerID;
    }
}
