package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum Variant {
    BLACK_6_SUITS, NO_VARIANT, RAINBOW_6_SUITS, THE_6_SUITS;

    @JsonValue
    public String toValue() {
        switch (this) {
        case BLACK_6_SUITS: return "Black (6 Suits)";
        case NO_VARIANT: return "No Variant";
        case RAINBOW_6_SUITS: return "Rainbow (6 Suits)";
        case THE_6_SUITS: return "6 Suits";
        }
        return null;
    }

    @JsonCreator
    public static Variant forValue(String value) throws IOException {
        if (value.equals("Black (6 Suits)")) return BLACK_6_SUITS;
        if (value.equals("No Variant")) return NO_VARIANT;
        if (value.equals("Rainbow (6 Suits)")) return RAINBOW_6_SUITS;
        if (value.equals("6 Suits")) return THE_6_SUITS;
        throw new IOException("Cannot deserialize Variant");
    }
}
