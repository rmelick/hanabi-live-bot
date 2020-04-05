package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.live.connector.schemas.java.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * A client that observes a single game
 * This should make it much easier to adapt the gamestate since we can get perfect information
 */
public class HanabiObserverClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiObserverClient.class.getName());
    private static final AtomicInteger BOT_COUNTER = new AtomicInteger(0);

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private GameState _gameState = new GameState();
    private final Long _gameID;
    private final String _gamePassword;

    public HanabiObserverClient(Long gameID, String gamePassword) {
        super("rolls-obs-g" + 0, "iamabot"); //BOT_COUNTER.getAndIncrement();
        _gameID = gameID;
        _gamePassword = gamePassword;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();

    }

    @Override
    public boolean handleCommand(String command, String body) throws IOException {
        super.handleCommand(command, body);
        switch (command) {
            case "table":
                return handleTableUpdate(_objectMapper.readValue(body, Table.class));
            case "tableStart":
                return handleTableStart(_objectMapper.readValue(body, TableStart.class));
            case "init":
                return handleInit(_objectMapper.readValue(body, Init.class));
            case "action":
                return handleAction();
            case "notify":
                return handleNotify(_objectMapper.readValue(body, Notify.class));
            case "tableProgress":
            case "user":
            case "userLeft":
            case "chat":
            case "tableGone":
            case "tableList":
                // ignore for now, it's super spammy
                return true;
            default:
                LOG.info(String.format("Unknown command %s %s", command, body));
                return true;
        }
    }

    /**
     * Updates have happened
     * @param readValue
     * @return
     */
    private boolean handleNotify(Notify readValue) {
        return true;
    }

    /**
     * it's our turn
     * @return
     */
    private boolean handleAction() {
        return true;
    }

    /**
     * tableStart  - reply hello
     * init - reply ready
     * action (once it's our turn)
     * @param init
     * @return
     */
    private boolean handleInit(Init init) {
        Ready ready = new Ready();
        String socketMessage = CommandParser.serialize("ready", ready);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        return true;
    }

    /**
     * Only received by a client connected to a game
     * @param tableStart
     * @return
     */
    private boolean handleTableStart(TableStart tableStart) {
        Hello command = new Hello();
        String socketMessage = CommandParser.serialize("hello", command);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        spectateTable(_gameID);
        return true;
    }

    /**
    Created or updated public/js/src/lobby/websocketInit.ts:97
     */
    private boolean handleTableUpdate(Table table) {
        if (table.getID() == _gameID) {
            _gameState.updateTable(table);
        }
        return true;
    }


    private void spectateTable(Long tableID) {
        TableSpectate command = new TableSpectate();
        command.setTableID(tableID);
        String socketMessage = CommandParser.serialize("spectateTable", command);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
    }
}
