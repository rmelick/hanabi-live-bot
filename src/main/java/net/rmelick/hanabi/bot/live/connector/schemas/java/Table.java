package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.*;

public class Table {
    private long id;
    private String name;
    private boolean password;
    private boolean joined;
    private long numPlayers;
    private boolean owned;
    private boolean running;
    private String variant;
    private boolean timed;
    private long baseTime;
    private long timePerTurn;
    private boolean sharedReplay;
    private long progress;
    private String players;
    private String spectators;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("password")
    public boolean getPassword() { return password; }
    @JsonProperty("password")
    public void setPassword(boolean value) { this.password = value; }

    @JsonProperty("joined")
    public boolean getJoined() { return joined; }
    @JsonProperty("joined")
    public void setJoined(boolean value) { this.joined = value; }

    @JsonProperty("numPlayers")
    public long getNumPlayers() { return numPlayers; }
    @JsonProperty("numPlayers")
    public void setNumPlayers(long value) { this.numPlayers = value; }

    @JsonProperty("owned")
    public boolean getOwned() { return owned; }
    @JsonProperty("owned")
    public void setOwned(boolean value) { this.owned = value; }

    @JsonProperty("running")
    public boolean getRunning() { return running; }
    @JsonProperty("running")
    public void setRunning(boolean value) { this.running = value; }

    @JsonProperty("variant")
    public String getVariant() { return variant; }
    @JsonProperty("variant")
    public void setVariant(String value) { this.variant = value; }

    @JsonProperty("timed")
    public boolean getTimed() { return timed; }
    @JsonProperty("timed")
    public void setTimed(boolean value) { this.timed = value; }

    @JsonProperty("baseTime")
    public long getBaseTime() { return baseTime; }
    @JsonProperty("baseTime")
    public void setBaseTime(long value) { this.baseTime = value; }

    @JsonProperty("timePerTurn")
    public long getTimePerTurn() { return timePerTurn; }
    @JsonProperty("timePerTurn")
    public void setTimePerTurn(long value) { this.timePerTurn = value; }

    @JsonProperty("sharedReplay")
    public boolean getSharedReplay() { return sharedReplay; }
    @JsonProperty("sharedReplay")
    public void setSharedReplay(boolean value) { this.sharedReplay = value; }

    @JsonProperty("progress")
    public long getProgress() { return progress; }
    @JsonProperty("progress")
    public void setProgress(long value) { this.progress = value; }

    @JsonProperty("players")
    public String getPlayers() { return players; }
    @JsonProperty("players")
    public void setPlayers(String value) { this.players = value; }

    @JsonProperty("spectators")
    public String getSpectators() { return spectators; }
    @JsonProperty("spectators")
    public void setSpectators(String value) { this.spectators = value; }
}
