package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Deck {
    private long suit;
    private long rank;

    @JsonProperty("suit")
    public long getSuit() { return suit; }
    @JsonProperty("suit")
    public void setSuit(long value) { this.suit = value; }

    @JsonProperty("rank")
    public long getRank() { return rank; }
    @JsonProperty("rank")
    public void setRank(long value) { this.rank = value; }
}