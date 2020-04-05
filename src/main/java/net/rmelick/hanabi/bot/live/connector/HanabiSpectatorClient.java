package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.ieee.LiveGameRunner;
import net.rmelick.hanabi.bot.live.connector.schemas.java.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * A client that observes a single game
 * This should make it much easier to adapt the gamestate since we can get perfect information
 */
public class HanabiSpectatorClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiSpectatorClient.class.getName());

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private MyGameState _myGameState = new MyGameState();
    private final AtomicBoolean _alreadySpectating = new AtomicBoolean(false);
    private final Long _gameID;
    private final LiveGameRunner _liveGameRunner;

    public HanabiSpectatorClient(String username, String userPassword, Long gameID, LiveGameRunner liveGameRunner) {
        super(username, userPassword);
        _gameID = gameID;
        _liveGameRunner = liveGameRunner;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();

    }

    @Override
    public boolean handleCommand(String command, String body) throws IOException {
        //LOG.info(String.format("Received command %s %s", command, body));
        switch (command) {
            case "table":
                return handleTableUpdate(_objectMapper.readValue(body, Table.class));
            case "tableStart":
                return handleTableStart(_objectMapper.readValue(body, TableStart.class));
            case "tableList":
                return handleTableList(_objectMapper.readValue(body, new TypeReference<List<Table>>() {}));
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
                // ignore for now, it's super spammy
                return true;
            default:
                LOG.info(String.format("Unknown command %s %s", command, body));
                return true;
        }
    }

    /**
     * Updates have happened
     * @param notify
     * @return
     */
    private boolean handleNotify(Notify notify) {
        switch (notify.getType()) {
            case "clue":
                return _liveGameRunner.recordClue(notify);
            case "text":
            case "status":
                return true; // don't care about display stuff
            default:
                return false;
        }
    }

    private boolean handleNotifyList(List<Notify> notifies) {
        _liveGameRunner.initialEvents(notifies);
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
        _liveGameRunner.init(init);
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
            if (table.getRunning()) {
                spectateTable(); // first time we see it's running, we join to spectate
            }
        }
        return true;
    }

    private boolean handleTableStart(TableStart tableStart) {
        Hello hello = new Hello();
        String socketMessage = "hello {}";
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        return true;
    }

    /**
     * When connecting, the table we want to spectate may already be in progress actually
     * @param tableList
     * @return
     */
    private boolean handleTableList(List<Table> tableList) {
        for (Table table : tableList) {
            handleTableUpdate(table);
        }
        return true;
    }


    public void spectateTable() {
        if (_alreadySpectating.getAndSet(true)) {
            LOG.warning("Already spectating");
        } else {
            TableSpectate command = new TableSpectate();
            command.setTableID(_gameID);
            String socketMessage = CommandParser.serialize("tableSpectate", command);
            LOG.info(String.format("Sending socket message %s", socketMessage));
            getWebSocket().sendText(socketMessage, true);
        }
    }
}
