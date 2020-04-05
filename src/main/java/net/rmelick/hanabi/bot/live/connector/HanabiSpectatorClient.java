package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.live.connector.schemas.java.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * A client that observes a single game
 * This should make it much easier to adapt the gamestate since we can get perfect information
 */
public class HanabiSpectatorClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiSpectatorClient.class.getName());
    private static final AtomicInteger BOT_COUNTER = new AtomicInteger(0);

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private MyGameState _myGameState = new MyGameState();
    private final Long _gameID;
    private Consumer<Init> _initCallback;
    private Consumer<List<Notify>> _notifyListCallback;

    public HanabiSpectatorClient(Long gameID) {
        super("rolls-obs-g" + 0, "iamabot"); //BOT_COUNTER.getAndIncrement();
        _gameID = gameID;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();

    }

    @Override
    public boolean handleCommand(String command, String body) throws IOException {
        LOG.info(String.format("Received command %s %s", command, body));
        super.handleCommand(command, body);
        switch (command) {
            case "table":
                return handleTableUpdate(_objectMapper.readValue(body, Table.class));
            case "tableStart":
                return false;
            case "init":
                return handleInit(_objectMapper.readValue(body, Init.class));
            case "action":
                return handleAction();
            case "notify":
                return handleNotify(_objectMapper.readValue(body, Notify.class));
            case "notifyList":
                return handleNotifyList(_objectMapper.readValue(body, new TypeReference<List<Notify>>() {}));
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
        return false;
    }

    private boolean handleNotifyList(List<Notify> notifies) {
        _notifyListCallback.accept(notifies);
        return true;
    }

    /**
     * it's our turn
     * @return
     */
    private boolean handleAction() {
        return false;
    }

    /**
     * tableStart  - reply hello
     * init - reply ready
     * action (once it's our turn)
     * @param init
     * @return
     */
    private boolean handleInit(Init init) {
        _initCallback.accept(init);
        Ready ready = new Ready();
        String socketMessage = CommandParser.serialize("ready", ready);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        return true;
    }

    /**
    Created or updated public/js/src/lobby/websocketInit.ts:97
     */
    private boolean handleTableUpdate(Table table) {
        if (table.getID() == _gameID) {
            _myGameState.updateTable(table);
        }
        return true;
    }


    public void spectateTable() {
        TableSpectate command = new TableSpectate();
        command.setTableID(_gameID);
        String socketMessage = CommandParser.serialize("tableSpectate", command);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
    }

    public void setInitCallback(Consumer<Init> initCallback) {
        _initCallback = initCallback;
    }

    public void setNotifyListCallback(Consumer<List<Notify>> notifyListCallback) {
        _notifyListCallback = notifyListCallback;
    }
}
