package net.rmelick.hanabi.bot.ieee;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
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
    private final Integer[] _ieeeToHanabi;
    private final BlockingQueue<Integer> _nextDrawSpot;

    public HandMapping(int numCardsForHand) {
        _ieeeToHanabi = new Integer[numCardsForHand];
        _nextDrawSpot = new LinkedBlockingQueue<>();
        for (int i = 0; i < numCardsForHand; i++) {
            _nextDrawSpot.add(i);
            _ieeeToHanabi[i] = null;
        }
    }

    public int recordHanabiDraw(int hanabiLiveOrder) {
        if (_nextDrawSpot.isEmpty()) {
            throw new IllegalStateException("Trying to draw, but we haven't recorded any discards or plays");
        }
        int slotTaken = _nextDrawSpot.remove();
        _ieeeToHanabi[slotTaken] = hanabiLiveOrder;
        return slotTaken;
    }


    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that hanabi card is in
     */
    public int findSlotOfHanabi(Long hanabiLiveOrder) {
        return ArrayUtils.indexOf(_ieeeToHanabi, hanabiLiveOrder.intValue());
    }

    public List<Integer> findSlotsOfHanabi(List<Long> hanabiLiveOrders) {
        return hanabiLiveOrders.stream().map(this::findSlotOfHanabi).collect(Collectors.toList());
    }

    public int getHanabiInSlot(int slot) {
        return _ieeeToHanabi[slot];
    }

    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that was freed up by the play
     */
    public int recordHanabiPlay(int hanabiLiveOrder) {
        int index = findSlotOfHanabi((long) hanabiLiveOrder);
        _ieeeToHanabi[index] = -1;
        _nextDrawSpot.add(index);
        return index;
    }

    public int recordHanabiDiscard(int hanabiLiveOrder) {
        // same cleanup logic as playing
        return recordHanabiPlay(hanabiLiveOrder);
    }
}
