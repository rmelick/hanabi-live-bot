package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.live.connector.schemas.java.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * A client that connects to a single game
 */
public class HanabiGameClient extends AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(HanabiGameClient.class.getName());
    private static final AtomicInteger BOT_COUNTER = new AtomicInteger(0);

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private final WorldState _worldState = new WorldState();
    private final Long _gameID;
    private final String _gamePassword;

    public HanabiGameClient(Long gameID, String gamePassword) {
        super("rolls-bot-g" + BOT_COUNTER.getAndIncrement(), "iamabot");
        _gameID = gameID;
        _gamePassword = gamePassword;
    }

    public void init() throws IOException, InterruptedException {
        super.connectWebsocket();
        joinTable(_gameID, _gamePassword);
    }

    @Override
    public boolean handleCommand(String command, String body) throws IOException {
        super.handleCommand(command, body);
        switch (command) {
            case "table":
                return handleTableUpdate(_objectMapper.readValue(body, Table.class));
            case "tableStart":
                return handleTableStart(_objectMapper.readValue(body, TableStart.class));
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
     * Only received by a client connected to a game
     * @param readValue
     * @return
     */
    private boolean handleTableStart(TableStart readValue) {
        Hello hello = new Hello();
        String socketMessage = CommandParser.serialize("hello", hello);
        LOG.info(String.format("Sending socket message %s", socketMessage));
        getWebSocket().sendText(socketMessage, true);
        return true;
    }

    /*
    Created or updated public/js/src/lobby/websocketInit.ts:97
     */
    private boolean handleTableUpdate(Table table) {
        _worldState.updateTable(table);
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
