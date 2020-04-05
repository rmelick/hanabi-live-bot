package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.ieee.LiveGameRunner;
import net.rmelick.hanabi.bot.live.connector.schemas.java.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * A client that connects to a single game
 */
public class HanabiPlayerClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiPlayerClient.class.getName());
    private static final AtomicInteger BOT_COUNTER = new AtomicInteger(0);

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private MyGameState _myGameState = new MyGameState();
    private final Long _gameID;
    private final String _gamePassword;
    private final LiveGameRunner _liveGameRunner;

    public HanabiPlayerClient(Long gameID, String gamePassword, LiveGameRunner liveGameRunner) {
        super("rolls-bot-g" + 0, "iamabot"); //BOT_COUNTER.getAndIncrement();
        _gameID = gameID;
        _gamePassword = gamePassword;
        _liveGameRunner = liveGameRunner;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();
        joinTable(_gameID, _gamePassword);
    }

    @Override
    public boolean handleCommand(String command, String body) throws IOException {
        LOG.info(String.format("Received command %s %s", command, body));
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
        Hello hello = new Hello();
        String socketMessage = "hello {}";
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        return true;
    }

    /**
     * If we re-join / resume a game, we need to know whose turn it is
     * @param notifies
     * @return
     */
    private boolean handleNotifyList(List<Notify> notifies) {
        // look for the most recent turn notification
        Collections.reverse(notifies);
        for (Notify notify : notifies) {
            if (notify.getType().equals("turn")) {
                LOG.info(String.format("Most recent turn is %s waiting for player %s", notify.getNum(), notify.getWho()));

            }
        }
        return true;
    }

    /*
    Created or updated public/js/src/lobby/websocketInit.ts:97
     */
    private boolean handleTableUpdate(Table table) {
        if (table.getID() == _gameID) {
            _myGameState.updateTable(table);
        }
        return true;
    }

    private void joinTable(Long tableID, String password) {
        TableJoin command = new TableJoin();
        command.setTableID(tableID);
        command.setPassword(hashPassword(password));
        String socketMessage = CommandParser.serialize("tableJoin", command);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
    }
}
