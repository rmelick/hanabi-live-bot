package net.rmelick.hanabi.bot.ieee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private final List<Integer> _ieeeToHanabi;
    private Queue<Integer> _nextDrawSpot;

    public HandMapping(int numCardsForHand) {
        _ieeeToHanabi = new ArrayList<>(numCardsForHand);
        _nextDrawSpot = new LinkedList<>();
        for (int i = 0; i < numCardsForHand; i++) {
            _nextDrawSpot.add(i);
            _ieeeToHanabi.add(-1);
        }
    }

    public int recordHanabiDraw(int hanabiLiveOrder) {
        int slotTaken = _nextDrawSpot.remove();
        _ieeeToHanabi.set(slotTaken, hanabiLiveOrder);
        return slotTaken;
    }


    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that hanabi card is in
     */
    public int findSlotOfHanabi(Long hanabiLiveOrder) {
        return _ieeeToHanabi.indexOf(hanabiLiveOrder.intValue());
    }

    public List<Integer> findSlotsOfHanabi(List<Long> hanabiLiveOrders) {
        return hanabiLiveOrders.stream().map(this::findSlotOfHanabi).collect(Collectors.toList());
    }

    public int getHanabiInSlot(int slot) {
        return _ieeeToHanabi.get(slot);
    }

    /**
     *
     * @param hanabiLiveOrder
     * @return the slot that was freed up by the play
     */
    public int recordHanabiPlay(int hanabiLiveOrder) {
        int index = _ieeeToHanabi.indexOf(hanabiLiveOrder);
        _ieeeToHanabi.remove(index);
        return index;
    }
}
