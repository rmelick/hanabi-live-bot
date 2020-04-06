package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * IEEE Agents draw to replace, and Hanabi Live doesn't really keep track of slots, it uses the overal
 * deck index as the card id
 */
class HandMapping {
    /**
     * Return the slot the card was put into
     * @param hanabiLiveOrder
     * @return
     */
    private final CardAdapter[] _ieeeToHanabi;
    private final BlockingQueue<Integer> _nextDrawSpot;
    private final CountDownLatch _drawsNeeded

    public HandMapping(int numCardsForHand) {
        _ieeeToHanabi = new CardAdapter[numCardsForHand];
        _nextDrawSpot = new LinkedBlockingQueue<>();
        for (int i = 0; i < numCardsForHand; i++) {
            _nextDrawSpot.add(i);
            _ieeeToHanabi[i] = null;
        }
    }

    public int recordHanabiDraw(CardAdapter card) {
        if (_nextDrawSpot.isEmpty()) {
            throw new IllegalStateException("Trying to draw, but we haven't recorded any discards or plays");
        }
        int slotTaken = _nextDrawSpot.remove();
        _ieeeToHanabi[slotTaken] = card;
        return slotTaken;
    }

    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that hanabi card is in
     */
    public int findSlotOfHanabi(Long hanabiLiveOrder) {
        for (int slot = 0; slot < _ieeeToHanabi.length; slot++) {
            if (_ieeeToHanabi[slot].getHanabiLiveOrder() == hanabiLiveOrder) {
                return slot;
            }
        }
        throw new IllegalStateException(String.format("Could not find hanabi card %s in hand %s", hanabiLiveOrder, Arrays.toString(_ieeeToHanabi)));
    }

    public List<Integer> findSlotsOfHanabi(List<Long> hanabiLiveOrders) {
        return hanabiLiveOrders.stream().map(this::findSlotOfHanabi).collect(Collectors.toList());
    }

    public int getHanabiInSlot(int slot) {
        CardAdapter cardAdapter = _ieeeToHanabi[slot];
        if (cardAdapter == null) {
            throw new IllegalStateException("Trying to access slot before deal");
        }
        return cardAdapter.getHanabiLiveOrder();
    }

    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that was freed up by the play
     */
    public int recordHanabiPlay(int hanabiLiveOrder) {
        int index = findSlotOfHanabi((long) hanabiLiveOrder);
        _ieeeToHanabi[index] = null;
        _nextDrawSpot.add(index);
        return index;
    }

    public int recordHanabiDiscard(int hanabiLiveOrder) {
        // same cleanup logic as playing
        return recordHanabiPlay(hanabiLiveOrder);
    }
}
