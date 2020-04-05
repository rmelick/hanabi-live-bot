package net.rmelick.hanabi.bot.live.connector;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A generic client that handles logging in and establishing the websocket
 */
public class AbstractHanabiClient {
    private static final Logger LOG = Logger.getLogger(AbstractHanabiClient.class.getName());
    private static final String HANABI_LIVE_LOGIN_URL = "https://hanabi.live/login";
    private static final String HANABI_LIVE_WEBSOCKET_URL = "wss://hanabi.live/ws";

    private final String _username;
    private final String _password;
    private WebSocket _webSocket = null;

    public AbstractHanabiClient(String username, String password) {
        _username = username;
        _password = password;
    }

    public WebSocket connectWebsocket() throws InterruptedException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("username", _username);
        data.put("password", _password);

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
        HttpResponse<String> loginResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (loginResponse.statusCode() != 200) {
            LOG.warning(String.format("Error when logging in %s, %s", loginResponse.statusCode(), loginResponse.body()));
        }

        //now that we have a session cookie, open the websocket
        _webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create(HANABI_LIVE_WEBSOCKET_URL), new WebsocketListener(this))
                .join();

        return _webSocket;
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

    public boolean handleCommand(String command, String body) throws IOException {
        //LOG.info(String.format("Received command %s body %s", command, body));
        return true;
    }

    /*
    From public/js/src/modals.ts:63 passwordSubmit
     */
    public static String hashPassword(String plainTextPassword) {
        String stringToHash = String.format("Hanabi game password %s", plainTextPassword);
        return DigestUtils.sha256Hex(stringToHash);
    }

    WebSocket getWebSocket() {
        return _webSocket;
    }

    public String getUsername() {
        return _username;
    }
}
