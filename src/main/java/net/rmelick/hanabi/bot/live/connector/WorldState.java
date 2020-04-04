package net.rmelick.hanabi.bot.live.connector;

import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;

import java.util.List;

public class WorldState {
    private List<Table> _currentTables = null;

    public List<Table> getCurrentTables() {
        return _currentTables;
    }

    public void updateTables(List<Table> tables) {
        _currentTables = tables;
    }
}
