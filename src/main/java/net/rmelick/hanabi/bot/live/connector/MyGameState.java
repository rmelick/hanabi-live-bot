package net.rmelick.hanabi.bot.live.connector;

import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;

public class MyGameState {
    private Init _internalState;
    
    public void init(Init initialState) {
        _internalState = initialState;
    }

    public void updateTable(Table table) {
    }
}
