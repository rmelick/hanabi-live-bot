package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.*;

public class Table {
    private long baseTime;
    private long id;
    private boolean joined;
    private String name;
    private long numPlayers;
    private boolean owned;
    private boolean password;
    private String players;
    private long progress;
    private boolean running;
    private boolean sharedReplay;
    private String spectators;
    private boolean timed;
    private long timePerTurn;
    private String variant;

    @JsonProperty("baseTime")
    public long getBaseTime() { return baseTime; }
    @JsonProperty("baseTime")
    public void setBaseTime(long value) { this.baseTime = value; }

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("joined")
    public boolean getJoined() { return joined; }
    @JsonProperty("joined")
    public void setJoined(boolean value) { this.joined = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("numPlayers")
    public long getNumPlayers() { return numPlayers; }
    @JsonProperty("numPlayers")
    public void setNumPlayers(long value) { this.numPlayers = value; }

    @JsonProperty("owned")
    public boolean getOwned() { return owned; }
    @JsonProperty("owned")
    public void setOwned(boolean value) { this.owned = value; }

    @JsonProperty("password")
    public boolean getPassword() { return password; }
    @JsonProperty("password")
    public void setPassword(boolean value) { this.password = value; }

    @JsonProperty("players")
    public String getPlayers() { return players; }
    @JsonProperty("players")
    public void setPlayers(String value) { this.players = value; }

    @JsonProperty("progress")
    public long getProgress() { return progress; }
    @JsonProperty("progress")
    public void setProgress(long value) { this.progress = value; }

    @JsonProperty("running")
    public boolean getRunning() { return running; }
    @JsonProperty("running")
    public void setRunning(boolean value) { this.running = value; }

    @JsonProperty("sharedReplay")
    public boolean getSharedReplay() { return sharedReplay; }
    @JsonProperty("sharedReplay")
    public void setSharedReplay(boolean value) { this.sharedReplay = value; }

    @JsonProperty("spectators")
    public String getSpectators() { return spectators; }
    @JsonProperty("spectators")
    public void setSpectators(String value) { this.spectators = value; }

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
