package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DeckBackedByHanabiLive extends Deck {
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
        Card topFromLive = null;
        try {
            topFromLive = _cards.poll(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException("Waited for new cards from live but the never came", e);
        }
        remove(topFromLive);
        return topFromLive;
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
