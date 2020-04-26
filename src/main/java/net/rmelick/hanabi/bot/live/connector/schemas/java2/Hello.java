package net.rmelick.hanabi.bot.live.connector.schemas.java2;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Hello {
    private boolean admin;
    private boolean firstTimeUser;
    private long id;
    private boolean muted;
    private Settings settings;
    private boolean shuttingDown;
    private long totalGames;
    private String username;
    private long version;

    @JsonProperty("admin")
    public boolean getAdmin() { return admin; }
    @JsonProperty("admin")
    public void setAdmin(boolean value) { this.admin = value; }

    @JsonProperty("firstTimeUser")
    public boolean getFirstTimeUser() { return firstTimeUser; }
    @JsonProperty("firstTimeUser")
    public void setFirstTimeUser(boolean value) { this.firstTimeUser = value; }

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("muted")
    public boolean getMuted() { return muted; }
    @JsonProperty("muted")
    public void setMuted(boolean value) { this.muted = value; }

    @JsonProperty("settings")
    public Settings getSettings() { return settings; }
    @JsonProperty("settings")
    public void setSettings(Settings value) { this.settings = value; }

    @JsonProperty("shuttingDown")
    public boolean getShuttingDown() { return shuttingDown; }
    @JsonProperty("shuttingDown")
    public void setShuttingDown(boolean value) { this.shuttingDown = value; }

    @JsonProperty("totalGames")
    public long getTotalGames() { return totalGames; }
    @JsonProperty("totalGames")
    public void setTotalGames(long value) { this.totalGames = value; }

    @JsonProperty("username")
    public String getUsername() { return username; }
    @JsonProperty("username")
    public void setUsername(String value) { this.username = value; }

    @JsonProperty("version")
    public long getVersion() { return version; }
    @JsonProperty("version")
    public void setVersion(long value) { this.version = value; }
}
