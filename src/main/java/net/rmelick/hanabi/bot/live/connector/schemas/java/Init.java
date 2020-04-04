
package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Init {
    private long baseTime;
    private boolean cardCycle;
    private List<Object> characterAssignments;
    private List<Object> characterMetadata;
    private long databaseID;
    private boolean deckPlays;
    private boolean emptyClues;
    private List<Object> hypoActions;
    private boolean hypothetical;
    private List<String> names;
    private boolean paused;
    private String pausePlayer;
    private boolean pauseQueued;
    private boolean replay;
    private long seat;
    private boolean sharedReplay;
    private boolean spectating;
    private boolean speedrun;
    private long tableID;
    private boolean timed;
    private long timePerTurn;
    private String variant;

    @JsonProperty("baseTime")
    public long getBaseTime() { return baseTime; }
    @JsonProperty("baseTime")
    public void setBaseTime(long value) { this.baseTime = value; }

    @JsonProperty("cardCycle")
    public boolean getCardCycle() { return cardCycle; }
    @JsonProperty("cardCycle")
    public void setCardCycle(boolean value) { this.cardCycle = value; }

    @JsonProperty("characterAssignments")
    public List<Object> getCharacterAssignments() { return characterAssignments; }
    @JsonProperty("characterAssignments")
    public void setCharacterAssignments(List<Object> value) { this.characterAssignments = value; }

    @JsonProperty("characterMetadata")
    public List<Object> getCharacterMetadata() { return characterMetadata; }
    @JsonProperty("characterMetadata")
    public void setCharacterMetadata(List<Object> value) { this.characterMetadata = value; }

    @JsonProperty("databaseID")
    public long getDatabaseID() { return databaseID; }
    @JsonProperty("databaseID")
    public void setDatabaseID(long value) { this.databaseID = value; }

    @JsonProperty("deckPlays")
    public boolean getDeckPlays() { return deckPlays; }
    @JsonProperty("deckPlays")
    public void setDeckPlays(boolean value) { this.deckPlays = value; }

    @JsonProperty("emptyClues")
    public boolean getEmptyClues() { return emptyClues; }
    @JsonProperty("emptyClues")
    public void setEmptyClues(boolean value) { this.emptyClues = value; }

    @JsonProperty("hypoActions")
    public List<Object> getHypoActions() { return hypoActions; }
    @JsonProperty("hypoActions")
    public void setHypoActions(List<Object> value) { this.hypoActions = value; }

    @JsonProperty("hypothetical")
    public boolean getHypothetical() { return hypothetical; }
    @JsonProperty("hypothetical")
    public void setHypothetical(boolean value) { this.hypothetical = value; }

    @JsonProperty("names")
    public List<String> getNames() { return names; }
    @JsonProperty("names")
    public void setNames(List<String> value) { this.names = value; }

    @JsonProperty("paused")
    public boolean getPaused() { return paused; }
    @JsonProperty("paused")
    public void setPaused(boolean value) { this.paused = value; }

    @JsonProperty("pausePlayer")
    public String getPausePlayer() { return pausePlayer; }
    @JsonProperty("pausePlayer")
    public void setPausePlayer(String value) { this.pausePlayer = value; }

    @JsonProperty("pauseQueued")
    public boolean getPauseQueued() { return pauseQueued; }
    @JsonProperty("pauseQueued")
    public void setPauseQueued(boolean value) { this.pauseQueued = value; }

    @JsonProperty("replay")
    public boolean getReplay() { return replay; }
    @JsonProperty("replay")
    public void setReplay(boolean value) { this.replay = value; }

    @JsonProperty("seat")
    public long getSeat() { return seat; }
    @JsonProperty("seat")
    public void setSeat(long value) { this.seat = value; }

    @JsonProperty("sharedReplay")
    public boolean getSharedReplay() { return sharedReplay; }
    @JsonProperty("sharedReplay")
    public void setSharedReplay(boolean value) { this.sharedReplay = value; }

    @JsonProperty("spectating")
    public boolean getSpectating() { return spectating; }
    @JsonProperty("spectating")
    public void setSpectating(boolean value) { this.spectating = value; }

    @JsonProperty("speedrun")
    public boolean getSpeedrun() { return speedrun; }
    @JsonProperty("speedrun")
    public void setSpeedrun(boolean value) { this.speedrun = value; }

    @JsonProperty("tableID")
    public long getTableID() { return tableID; }
    @JsonProperty("tableID")
    public void setTableID(long value) { this.tableID = value; }

    @JsonProperty("timed")
    public boolean getTimed() { return timed; }
    @JsonProperty("timed")
    public void setTimed(boolean value) { this.timed = value; }

    @JsonProperty("timePerTurn")
    public long getTimePerTurn() { return timePerTurn; }
    @JsonProperty("timePerTurn")
    public void setTimePerTurn(long value) { this.timePerTurn = value; }

    @JsonProperty("variant")
    public String getVariant() { return variant; }
    @JsonProperty("variant")
    public void setVariant(String value) { this.variant = value; }
}