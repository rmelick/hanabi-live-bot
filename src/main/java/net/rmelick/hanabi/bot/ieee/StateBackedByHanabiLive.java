package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.BasicState;

import java.lang.reflect.Field;

public class StateBackedByHanabiLive extends BasicState {
    public StateBackedByHanabiLive(int playerCount) {
        super(playerCount);
        try {
            Field deckField = BasicState.class.getDeclaredField("deck");
            deckField.setAccessible(true);
            deckField.set(this, new DeckBackedByHanabiLive());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
