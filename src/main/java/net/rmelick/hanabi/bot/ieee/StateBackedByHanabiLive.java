package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.Card;
import com.fossgalaxy.games.fireworks.state.Deck;

public class StateBackedByHanabiLive extends BasicState {
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
        return super.drawFromDeck();
    }

    @Override
    public Deck getDeck() {
        // a lot of ai agents call this method, so maybe override it too?
        return super.getDeck();
    }
}
