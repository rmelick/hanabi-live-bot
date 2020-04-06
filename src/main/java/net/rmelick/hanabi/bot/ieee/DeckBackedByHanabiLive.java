package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DeckBackedByHanabiLive extends Deck {
    private static final Logger LOG = Logger.getLogger(DeckBackedByHanabiLive.class.getName());
    private final BlockingQueue<Card> _cards = new LinkedBlockingQueue<>();

    @Override
    public void shuffle() {
        //
    }

    @Override
    public void shuffle(long seed) {
        //
    }

    @Override
    public Card getTopCard() {
        try {
            Card topFromLive = _cards.poll(2, TimeUnit.MINUTES);
            if (topFromLive == null) {
                LOG.warning("Timeout reached waiting for new cards from live but they never came ");
                throw new IllegalStateException("Waited for new cards from live but they never came");
            }
            remove(topFromLive); // don't forget to remove from the underlying deck so the other math functions work out correctly
            return topFromLive;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException("Waited for new cards from live but the never came", e);
        }
    }

    /**
     * When a card is drawn from hanabi live, record it here
     * @param card
     */
    public void recordDrawFromLive(Card card) {
        // make it available so whoever wants to draw it next can draw it
        _cards.add(card);
    }
}
