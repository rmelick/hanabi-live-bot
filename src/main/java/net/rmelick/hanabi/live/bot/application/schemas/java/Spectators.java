package net.rmelick.hanabi.live.bot.application.schemas.java;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum Spectators {
    A4_COLEMAN_TRIPPY_MCFLY, ANA97, BIGHITS2000_JPOPS, EMPTY, KD_SC1288_SHRU_KHUSHI, NATHALIE3593_THE_KAITO_KID_MARCOGILBERT;

    @JsonValue
    public String toValue() {
        switch (this) {
        case A4_COLEMAN_TRIPPY_MCFLY: return "a4coleman, trippy_mcfly";
        case ANA97: return "Ana97";
        case BIGHITS2000_JPOPS: return "bighits2000, jpops";
        case EMPTY: return "-";
        case KD_SC1288_SHRU_KHUSHI: return "kd, sc1288, Shru, khushi";
        case NATHALIE3593_THE_KAITO_KID_MARCOGILBERT: return "nathalie3593, The Kaito Kid, marcogilbert";
        }
        return null;
    }

    @JsonCreator
    public static Spectators forValue(String value) throws IOException {
        if (value.equals("a4coleman, trippy_mcfly")) return A4_COLEMAN_TRIPPY_MCFLY;
        if (value.equals("Ana97")) return ANA97;
        if (value.equals("bighits2000, jpops")) return BIGHITS2000_JPOPS;
        if (value.equals("-")) return EMPTY;
        if (value.equals("kd, sc1288, Shru, khushi")) return KD_SC1288_SHRU_KHUSHI;
        if (value.equals("nathalie3593, The Kaito Kid, marcogilbert")) return NATHALIE3593_THE_KAITO_KID_MARCOGILBERT;
        throw new IOException("Cannot deserialize Spectators");
    }
}
