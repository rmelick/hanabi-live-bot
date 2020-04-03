package net.rmelick.hanabi.live.bot.application;

import org.glassfish.tyrus.client.ClientManager;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.websocket.*;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class WebsocketTest {
    private static final String HANABI_LIVE_WEBSOCKET_URL = "wss://hanabi.live/ws";

    public void run() {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new SimpleMessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(HANABI_LIVE_WEBSOCKET_URL, sessionHandler);
    }

    private static CountDownLatch messageLatch;
    private static final String SENT_MESSAGE = "Hello World";

    private static String login() {
        try {
            String loginBase = "https://hanabi.live/login";
            String username = "ryanb";
            String password = "iamabot";
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("password", password);

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            HttpClient client = HttpClient.newBuilder()
                    .cookieHandler(CookieHandler.getDefault())
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(loginBase))
                    .timeout(Duration.ofMinutes(2))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(ofFormData(data))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return "session";
        } catch (IOException | InterruptedException e) {
            // do something sensible
            return null;
        }
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<String, Object> data) {
        var builder = new StringBuilder();
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

    public static void runJava11() throws InterruptedException, IOException {
        String loginBase = "https://hanabi.live/login";
        String username = "ryanb";
        String password = "iamabot";
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        CookieManager cookieManager = new CookieManager(new InMemoryNoMaxCookieStore(), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginBase))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(data))
                .build();

        HttpResponse<String> loginResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Received login Response" + loginResponse.statusCode());
        System.out.println(loginResponse.headers().firstValue("set-cookie"));

        HttpRequest secondRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://hanabi.live/public/webfonts/source-sans-pro-v11-latin-300italic.woff2"))
                .GET()
                .build();

        cookieManager.getCookieStore().getCookies();
        HttpResponse<String> secondResponse = client.send(secondRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Received second Response" + secondResponse.statusCode());
        //now that we have a session cookie, open the websocket
        WebSocket webSocket = client.newWebSocketBuilder().buildAsync(URI.create(HANABI_LIVE_WEBSOCKET_URL), getListener()).join();
        webSocket.sendText("hello from the client", true);

        TimeUnit.SECONDS.sleep(10000);
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok");
    }

    private static WebSocket.Listener getListener() {
        return new WebSocket.Listener() {
            @Override
            public CompletionStage<?> onText(WebSocket webSocket,
                                             CharSequence data, boolean last) {

                System.out.println("onText: " + data);

                return WebSocket.Listener.super.onText(webSocket, data, last);
            }

            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("onOpen");
                WebSocket.Listener.super.onOpen(webSocket);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode,
                                              String reason) {
                System.out.println("onClose: " + statusCode + " " + reason);
                return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.out.println("onError: " + error);
                WebSocket.Listener.super.onError(webSocket, error);
            }

            @Override
            public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
                System.out.println("onBinary: " + data);
                return WebSocket.Listener.super.onBinary(webSocket, data, last);
            }

            @Override
            public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
                System.out.println("onPing: " + message);
                return WebSocket.Listener.super.onPing(webSocket, message);
            }

            @Override
            public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
                System.out.println("onPong: " + message);
                return WebSocket.Listener.super.onPong(webSocket, message);
            }
        };
    }

    public static void runTyrus(){
        try {
            messageLatch = new CountDownLatch(1);

            final ClientEndpointConfig cec =
                    ClientEndpointConfig.Builder.create().configurator(
                            new ClientEndpointConfig.Configurator() {
                                @Override
                                public void beforeRequest(Map<String, List<String>> headers) {
                                    super.beforeRequest(headers);
                                    String sessionId = login();
                                    List cookieList = headers.get("Cookie");
                                    if (cookieList == null) cookieList = new ArrayList();
                                    cookieList.add("SESSIONID=\""+sessionId+"\"");
                                    headers.put("Cookie", cookieList);
                                }
                            }).build();

            ClientManager client = ClientManager.createClient();
            client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    try {
                        session.addMessageHandler(new MessageHandler.Whole<String>() {

                            @Override
                            public void onMessage(String message) {
                                System.out.println("Received message: "+message);
                                messageLatch.countDown();
                            }
                        });
                        session.getBasicRemote().sendText(SENT_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, cec, new URI(HANABI_LIVE_WEBSOCKET_URL));
            messageLatch.await(100, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //new WebsocketTest().run();
        //runTyrus();
        try {
            runJava11();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}
