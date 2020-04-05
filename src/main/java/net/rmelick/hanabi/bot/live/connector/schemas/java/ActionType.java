package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.HashMap;
import java.util.Map;

/**
 * const (
 * 	actionTypeClue = iota
 * 	actionTypePlay
 * 	actionTypeDiscard
 * 	actionTypeDeckPlay
 * 	actionTypeTimeLimitReached
 * 	actionTypeIdleLimitReached
 * )
 *
 * // TODO remove the above constants after the database transition
 * const (
 * 	actionType2Play = iota
 * 	actionType2Discard
 * 	actionType2ColorClue
 * 	actionType2NumberClue
 * )
 */
public enum ActionType {
    CLUE(0),
    PLAY(1),
    DISCARD(2);

    private final int _hanabiLiveID;

    ActionType(int hanabiLiveID) {
        _hanabiLiveID = hanabiLiveID;
    }

    private static final Map<Integer, ActionType> BY_HANABI_ID = new HashMap<>();

    static {
        for (ActionType e: values()) {
            BY_HANABI_ID.put(e._hanabiLiveID, e);
        }
    }

    public static ActionType valueOfHanabiID(int hanabiLiveID) {
        return BY_HANABI_ID.get(hanabiLiveID);
    }

    public int getHanabiLiveID() {
        return _hanabiLiveID;
    }
}
