package net.rmelick.hanabi.bot.live.connector.schemas.java2;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Deck {
    private long rank;
    private long suit;

    @JsonProperty("rank")
    public long getRank() { return rank; }
    @JsonProperty("rank")
    public void setRank(long value) { this.rank = value; }

    @JsonProperty("suit")
    public long getSuit() { return suit; }
    @JsonProperty("suit")
    public void setSuit(long value) { this.suit = value; }
}
