package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.CardColour;

import java.util.Map;

public class CardColors {
    private static final Map<String, CardColour> MAP = Map.of(
            "Red", CardColour.RED,
            "Yellow", CardColour.ORANGE,
            "Green", CardColour.GREEN,
            "Blue", CardColour.BLUE,
            "Purple", CardColour.WHITE
    );

    private static final String[] DEFAULT_VARIANT = new String[] {"Red", "Yellow", "Green", "Blue", "Purple"};

    public static CardColour getFromLiveId(Long suitID) {
        return MAP.get(DEFAULT_VARIANT[suitID.intValue()]);
    }
}
