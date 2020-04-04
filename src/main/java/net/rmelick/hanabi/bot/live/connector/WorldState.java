package net.rmelick.hanabi.bot.live.connector;

import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldState {
    private Map<Long, Table> _currentTables = new ConcurrentHashMap<>();

    public List<Table> getCurrentTables() {
        return new ArrayList<>(_currentTables.values());
    }

    public void initializeTables(List<Table> tables) {
        _currentTables.clear();
        for (Table table : tables) {
            _currentTables.put(table.getID(), table);
        }
    }

    public void updateTable(Table table) {
        _currentTables.put(table.getID(), table);
    }

    public void removeTable(long tableID) {
        _currentTables.remove(tableID);
    }
}
