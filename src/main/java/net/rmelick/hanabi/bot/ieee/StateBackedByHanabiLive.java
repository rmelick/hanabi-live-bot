package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;
import net.rmelick.hanabi.bot.live.connector.HanabiPlayerClient;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StateBackedByHanabiLive extends BasicState {
    private static final java.util.logging.Logger LOG = Logger.getLogger(StateBackedByHanabiLive.class.getName());

    private final DeckBackedByHanabiLive _deckBackedByHanabiLive = new DeckBackedByHanabiLive();


    public StateBackedByHanabiLive(int playerCount) {
        super(playerCount);
        try {
            Field deckField = BasicState.class.getDeclaredField("deck");
            deckField.setAccessible(true);
            deckField.set(this, _deckBackedByHanabiLive);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Could not modify deck property", e);
        }
    }

    public void recordDrawFromLive(Card card) {
        _deckBackedByHanabiLive.recordDrawFromLive(card);
    }
}
