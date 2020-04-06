package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.Card;

public class CardAdapter {
    private final Card _card;
    private int _hanabiLiveOrder;

    public CardAdapter(Card card, int hanabiLiveOrder) {
        _card = card;
        _hanabiLiveOrder = hanabiLiveOrder;
    }

    public int getHanabiLiveOrder() {
        return _hanabiLiveOrder;
    }

    @Override
    public String toString() {
        return "CardAdapter{" +
                "color=" + CardColors.getLiveColor(_card.colour) +
                ", card=" + _card +
                ", hanabiLiveOrder=" + _hanabiLiveOrder +
                '}';
    }
}
