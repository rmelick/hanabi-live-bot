package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.ieee.LiveGameRunner;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Action;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Hello;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Notify;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Ready;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;
import net.rmelick.hanabi.bot.live.connector.schemas.java.TableJoin;
import net.rmelick.hanabi.bot.live.connector.schemas.java.TableStart;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Type;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A client that connects to a single game
 */
public class HanabiPlayerClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiPlayerClient.class.getName());

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private MyGameState _myGameState = new MyGameState();
    private final Long _gameID;
    private final String _gamePassword;
    private final LiveGameRunner _liveGameRunner;
    private Integer _myPositionInGame;

    public HanabiPlayerClient(String username, String userPassword, Long gameID, String gamePassword, LiveGameRunner liveGameRunner) {
        super(username, userPassword);
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
            case "sound":
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
            case TURN:
                return handleNotifyTurn(notify);
            case CLUE:
            case STRIKE:
                return true; // handled by the Spectator
            case TEXT:
            case STATUS:
                return true; // don't care about display stuff
            default:
                throw new IllegalArgumentException("Invalid Notify");
        }
    }

    /**
     * it's our turn
     * @return
     */
    private boolean handleAction() {
        LOG.info("Received a message saying it's my turn, but that should already be covered by the notify 'turn' message");
        //return takeTurn();
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
        _myPositionInGame = init.getNames().indexOf(getUsername());
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
            if (notify.getType().equals(Type.TURN)) {
                LOG.info(String.format("Most recent turn is %s waiting for player %s", notify.getNum(), notify.getWho()));
                return handleNotifyTurn(notify);
            }
        }
        return true;
    }

    private boolean handleNotifyTurn(Notify notify) {
        if (_myPositionInGame == null) {
            throw new IllegalStateException("Received turn notification but don't know my own position");
        }
        else if (_myPositionInGame == notify.getWho().intValue()) {
            return takeTurn();
        } else {
            LOG.info(String.format("Got a turn notification (player %s), but not for me (me %s)", notify.getWho(), _myPositionInGame));
            return true;
        }
    }

    private boolean takeTurn() {
        LOG.info("It's my turn!!!");
        Action nextMove = _liveGameRunner.getNextPlayerMove();
        String socketMessage = CommandParser.serialize("action", nextMove);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
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
