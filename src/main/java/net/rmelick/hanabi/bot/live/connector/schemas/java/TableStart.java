package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableStart {
    private boolean replay;

    @JsonProperty("replay")
    public boolean getReplay() { return replay; }
    @JsonProperty("replay")
    public void setReplay(boolean value) { this.replay = value; }
}