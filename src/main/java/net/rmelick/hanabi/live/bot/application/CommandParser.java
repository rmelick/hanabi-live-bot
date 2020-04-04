package net.rmelick.hanabi.live.bot.application;

import java.io.IOException;

public class CommandParser {
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
}
