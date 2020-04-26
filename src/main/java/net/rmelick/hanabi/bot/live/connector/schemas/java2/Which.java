package net.rmelick.hanabi.bot.live.connector.schemas.java2;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Which {
    private long index;
    private long order;
    private long rank;
    private long suit;

    @JsonProperty("index")
    public long getIndex() { return index; }
    @JsonProperty("index")
    public void setIndex(long value) { this.index = value; }

    @JsonProperty("order")
    public long getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(long value) { this.order = value; }

    @JsonProperty("rank")
    public long getRank() { return rank; }
    @JsonProperty("rank")
    public void setRank(long value) { this.rank = value; }

    @JsonProperty("suit")
    public long getSuit() { return suit; }
    @JsonProperty("suit")
    public void setSuit(long value) { this.suit = value; }
}
