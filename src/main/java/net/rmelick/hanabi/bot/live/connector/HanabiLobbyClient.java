package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Hello;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;
import net.rmelick.hanabi.bot.live.connector.schemas.java.TableGone;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * A client that connects to the lobby
 */
public class HanabiLobbyClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiLobbyClient.class.getName());

    private final ObjectMapper _objectMapper = HanabiObjectMapper.getObjectMapper();
    private final WorldState _worldState = new WorldState();

    // TODO temp for easy debugging
    private final ActiveGamesManager _activeGamesManager;

    public HanabiLobbyClient(ActiveGamesManager activeGamesManager) {
        super("rolls-bot", "iamabot");
        _activeGamesManager = activeGamesManager;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();
    }

    public boolean handleCommand(String command, String body) throws IOException {
        switch (command) {
            case "hello":
                return handleHello(_objectMapper.readValue(body, Hello.class));
            case "tableList":
                return handleTableList(_objectMapper.readValue(body, new TypeReference<List<Table>>() { }));
            case "table":
                return handleTableUpdate(_objectMapper.readValue(body, Table.class));
            case "tableGone":
                return handleTableGone(_objectMapper.readValue(body, TableGone.class));
            case "tableProgress":
            case "user":
            case "userLeft":
            case "chat":
                // ignore for now, it's super spammy
                return true;
            default:
                LOG.info(String.format("Unknown command %s %s", command, body));
                return true;
        }
    }

    private boolean handleHello(Hello hello) {
        return true;
    }

    private boolean handleTableList(List<Table> tables) {
        _worldState.initializeTables(tables);
        return true;
    }

    /*
    Created or updated public/js/src/lobby/websocketInit.ts:97
     */
    private boolean handleTableUpdate(Table table) {
        _worldState.updateTable(table);
        return true;
    }

    private boolean handleTableGone(TableGone tableGone) {
        _worldState.removeTable(tableGone.getID());
        return true;
    }

    public WorldState getWorldState() {
        return _worldState;
    }
}
