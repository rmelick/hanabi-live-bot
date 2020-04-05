package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.HashMap;
import java.util.Map;

/**
 * const (
 * 	clueTypeRank = iota
 * 	clueTypeColor
 * )
 */
public enum ClueType {
    RANK(0),
    COLOR(1);

    private final int _hanabiLiveID;

    ClueType(int hanabiLiveID) {
        _hanabiLiveID = hanabiLiveID;
    }

    private static final Map<Integer, ClueType> BY_HANABI_ID = new HashMap<>();

    static {
        for (ClueType e: values()) {
            BY_HANABI_ID.put(e._hanabiLiveID, e);
        }
    }

    public static ClueType valueOfHanabiID(int hanabiLiveID) {
        return BY_HANABI_ID.get(hanabiLiveID);
    }

    public int getHanabiLiveID() {
        return _hanabiLiveID;
    }
}
