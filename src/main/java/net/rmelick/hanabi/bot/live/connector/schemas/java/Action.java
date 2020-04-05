package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Action {
    private long type;
    private long target;
    private Clue clue;

    @JsonProperty("type")
    public long getType() { return type; }
    @JsonProperty("type")
    public void setType(long value) { this.type = value; }

    @JsonProperty("target")
    public long getTarget() { return target; }
    @JsonProperty("target")
    public void setTarget(long value) { this.target = value; }

    @JsonProperty("clue")
    public Clue getClue() { return clue; }
    @JsonProperty("clue")
    public void setClue(Clue value) { this.clue = value; }
}