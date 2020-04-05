package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Type {
    CLUE, DECK_ORDER, DISCARD, DRAW, PLAY, STATUS, STRIKE, TEXT, TURN;

    @JsonValue
    public String toValue() {
        switch (this) {
            case CLUE: return "clue";
            case DECK_ORDER: return "deckOrder";
            case DISCARD: return "discard";
            case DRAW: return "draw";
            case PLAY: return "play";
            case STATUS: return "status";
            case STRIKE: return "strike";
            case TEXT: return "text";
            case TURN: return "turn";
        }
        return null;
    }

    @JsonCreator
    public static Type forValue(String value) throws IOException {
        if (value.equals("clue")) return CLUE;
        if (value.equals("deckOrder")) return DECK_ORDER;
        if (value.equals("discard")) return DISCARD;
        if (value.equals("draw")) return DRAW;
        if (value.equals("play")) return PLAY;
        if (value.equals("status")) return STATUS;
        if (value.equals("strike")) return STRIKE;
        if (value.equals("text")) return TEXT;
        if (value.equals("turn")) return TURN;
        throw new IOException("Cannot deserialize Type");
    }
}