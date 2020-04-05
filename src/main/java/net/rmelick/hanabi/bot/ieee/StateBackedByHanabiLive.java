package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StateBackedByHanabiLive extends BasicState {
    private final BlockingQueue<Card> _nextDrawQueue = new LinkedBlockingQueue<>();
    public StateBackedByHanabiLive(int playerCount) {
        super(playerCount);
        /*try {
            Field deckField = BasicState.class.getDeclaredField("deck");
            deckField.setAccessible(true);
            deckField.set(this, new DeckBackedByHanabiLive());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public Card drawFromDeck() {
        // Both the DiscardCard and PlayCard actions just call this method, so maybe we don't need to modify the deckField?
        try {
            Card nextCard = _nextDrawQueue.poll(2, TimeUnit.MINUTES);
            super.getDeck().remove(nextCard);
            return nextCard;
        } catch (InterruptedException e) {
            throw new IllegalStateException("Timed out trying to draw", e);
        }
    }

    @Override
    public Deck getDeck() {
        // a lot of ai agents call this method, so maybe override it too?
        return super.getDeck();
    }

    public void makeCardAvailableToDraw(Card card) {
        _nextDrawQueue.add(card);
    }
}
