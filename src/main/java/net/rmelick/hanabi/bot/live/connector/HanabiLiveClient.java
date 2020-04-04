package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Hello;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Table;
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

public class HanabiLiveClient {
    private static final String HANABI_LIVE_LOGIN_URL = "https://hanabi.live/login";
    private static final String HANABI_LIVE_WEBSOCKET_URL = "wss://hanabi.live/ws";

    private final ObjectMapper _objectMapper = new ObjectMapper();
    private final WorldState _worldState = new WorldState();
    /*
    From public/js/src/modals.ts:63 passwordSubmit
     */
    private static String hashPassword(String plainTextPassword) {
        String stringToHash = String.format("Hanabi game password %s", plainTextPassword);
        return DigestUtils.sha256Hex(stringToHash);
    }

    public WebSocket connectWebsocket() throws InterruptedException, IOException {
        String username = "ryanb";
        String password = "iamabot";
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        CookieManager cookieManager = new CookieManager(new InMemoryNoMaxCookieStore(),
                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HANABI_LIVE_LOGIN_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(data))
                .build();

        // we can ignore the response from login, as all it does it set the cookies we want
        client.send(request, HttpResponse.BodyHandlers.ofString());

        //now that we have a session cookie, open the websocket
        return client.newWebSocketBuilder()
                .buildAsync(URI.create(HANABI_LIVE_WEBSOCKET_URL), new WebsocketListener(this))
                .join();
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new HanabiLiveClient().connectWebsocket();
        Scanner userInput = new Scanner(System.in);
        while(true) {
            String input = userInput.nextLine();
        }
    }

    public boolean handleCommand(String command, String body) throws IOException {
        switch (command) {
            case "hello":
                return handleHello(_objectMapper.readValue(body, Hello.class));
            case "tableList":
                return handleTableList(_objectMapper.readValue(body, new TypeReference<List<Table>>() { }));
            default:
                System.out.println(String.format("Unknown command %s %s", command, body));
                return false;
        }
    }

    private boolean handleHello(Hello hello) {
        return true;
    }

    private boolean handleTableList(List<Table> tables) {
        _worldState.updateTables(tables);
        return true;
    }

    public WorldState getWorldState() {
        return _worldState;
    }
}
