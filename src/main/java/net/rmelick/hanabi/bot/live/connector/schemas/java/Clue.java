package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Clue {
    private long type;
    private long value;

    @JsonProperty("type")
    public long getType() { return type; }
    @JsonProperty("type")
    public void setType(long value) { this.type = value; }

    @JsonProperty("value")
    public long getValue() { return value; }
    @JsonProperty("value")
    public void setValue(long value) { this.value = value; }
}