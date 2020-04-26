package net.rmelick.hanabi.bot.live.connector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandParser {
    private static final ObjectMapper mapper = HanabiObjectMapper.getObjectMapper();

    public static ParsedCommand parseCommand(String commandAndBody) {
        String[] pieces = commandAndBody.split(" ", 2);
        if (pieces.length < 2) {
            System.out.println("Empty command: " + commandAndBody);
            return null;
        }
        String command = pieces[0];
        String body = pieces[1];
        return new ParsedCommand(command, body);
    }

    public static class ParsedCommand {
        String command;
        String body;

        public ParsedCommand(String command, String body) {
            this.command = command;
            this.body = body;
        }
    }

    public static String serialize(String command, Object body) {
        try {
            return command + " " + mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
