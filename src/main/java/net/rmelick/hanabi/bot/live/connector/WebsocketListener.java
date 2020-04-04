package net.rmelick.hanabi.bot.live.connector;

import java.io.IOException;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

class WebsocketListener implements WebSocket.Listener {
    private final HanabiLiveClient _client;
    private StringBuffer _incomingText;

    public WebsocketListener(HanabiLiveClient client) {
        _client = client;
        _incomingText = new StringBuffer();
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket,
                                     CharSequence data, boolean last) {
        _incomingText.append(data);

        // check jdk.httpclient.websocket properties to see if we can set them so we don't need this silly aggregating
        if (!last) {
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        String fullData = _incomingText.toString();
        _incomingText = new StringBuffer();
        CommandParser.ParsedCommand command = CommandParser.parseCommand(fullData);
        try {
            if (command != null) {
                _client.handleCommand(command.command, command.body);
            } else {
                System.out.println("Empty command");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
}
