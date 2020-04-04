package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {
    private boolean soundMove;
    private boolean soundTimer;
    private boolean keldonMode;
    private boolean colorblindMode;
    private boolean realLifeMode;
    private boolean reverseHands;
    private boolean styleNumbers;
    private boolean showTimerInUntimed;
    private long volume;
    private boolean speedrunPreplay;
    private boolean speedrunMode;
    private boolean hyphenatedConventions;
    private String createTableVariant;
    private boolean createTableTimed;
    private long createTableBaseTimeMinutes;
    private long createTableTimePerTurnSeconds;
    private boolean createTableSpeedrun;
    private boolean createTableCardCycle;
    private boolean createTableDeckPlays;
    private boolean createTableEmptyClues;
    private boolean createTableCharacterAssignments;
    private boolean createTableAlertWaiters;

    @JsonProperty("soundMove")
    public boolean getSoundMove() { return soundMove; }
    @JsonProperty("soundMove")
    public void setSoundMove(boolean value) { this.soundMove = value; }

    @JsonProperty("soundTimer")
    public boolean getSoundTimer() { return soundTimer; }
    @JsonProperty("soundTimer")
    public void setSoundTimer(boolean value) { this.soundTimer = value; }

    @JsonProperty("keldonMode")
    public boolean getKeldonMode() { return keldonMode; }
    @JsonProperty("keldonMode")
    public void setKeldonMode(boolean value) { this.keldonMode = value; }

    @JsonProperty("colorblindMode")
    public boolean getColorblindMode() { return colorblindMode; }
    @JsonProperty("colorblindMode")
    public void setColorblindMode(boolean value) { this.colorblindMode = value; }

    @JsonProperty("realLifeMode")
    public boolean getRealLifeMode() { return realLifeMode; }
    @JsonProperty("realLifeMode")
    public void setRealLifeMode(boolean value) { this.realLifeMode = value; }

    @JsonProperty("reverseHands")
    public boolean getReverseHands() { return reverseHands; }
    @JsonProperty("reverseHands")
    public void setReverseHands(boolean value) { this.reverseHands = value; }

    @JsonProperty("styleNumbers")
    public boolean getStyleNumbers() { return styleNumbers; }
    @JsonProperty("styleNumbers")
    public void setStyleNumbers(boolean value) { this.styleNumbers = value; }

    @JsonProperty("showTimerInUntimed")
    public boolean getShowTimerInUntimed() { return showTimerInUntimed; }
    @JsonProperty("showTimerInUntimed")
    public void setShowTimerInUntimed(boolean value) { this.showTimerInUntimed = value; }

    @JsonProperty("volume")
    public long getVolume() { return volume; }
    @JsonProperty("volume")
    public void setVolume(long value) { this.volume = value; }

    @JsonProperty("speedrunPreplay")
    public boolean getSpeedrunPreplay() { return speedrunPreplay; }
    @JsonProperty("speedrunPreplay")
    public void setSpeedrunPreplay(boolean value) { this.speedrunPreplay = value; }

    @JsonProperty("speedrunMode")
    public boolean getSpeedrunMode() { return speedrunMode; }
    @JsonProperty("speedrunMode")
    public void setSpeedrunMode(boolean value) { this.speedrunMode = value; }

    @JsonProperty("hyphenatedConventions")
    public boolean getHyphenatedConventions() { return hyphenatedConventions; }
    @JsonProperty("hyphenatedConventions")
    public void setHyphenatedConventions(boolean value) { this.hyphenatedConventions = value; }

    @JsonProperty("createTableVariant")
    public String getCreateTableVariant() { return createTableVariant; }
    @JsonProperty("createTableVariant")
    public void setCreateTableVariant(String value) { this.createTableVariant = value; }

    @JsonProperty("createTableTimed")
    public boolean getCreateTableTimed() { return createTableTimed; }
    @JsonProperty("createTableTimed")
    public void setCreateTableTimed(boolean value) { this.createTableTimed = value; }

    @JsonProperty("createTableBaseTimeMinutes")
    public long getCreateTableBaseTimeMinutes() { return createTableBaseTimeMinutes; }
    @JsonProperty("createTableBaseTimeMinutes")
    public void setCreateTableBaseTimeMinutes(long value) { this.createTableBaseTimeMinutes = value; }

    @JsonProperty("createTableTimePerTurnSeconds")
    public long getCreateTableTimePerTurnSeconds() { return createTableTimePerTurnSeconds; }
    @JsonProperty("createTableTimePerTurnSeconds")
    public void setCreateTableTimePerTurnSeconds(long value) { this.createTableTimePerTurnSeconds = value; }

    @JsonProperty("createTableSpeedrun")
    public boolean getCreateTableSpeedrun() { return createTableSpeedrun; }
    @JsonProperty("createTableSpeedrun")
    public void setCreateTableSpeedrun(boolean value) { this.createTableSpeedrun = value; }

    @JsonProperty("createTableCardCycle")
    public boolean getCreateTableCardCycle() { return createTableCardCycle; }
    @JsonProperty("createTableCardCycle")
    public void setCreateTableCardCycle(boolean value) { this.createTableCardCycle = value; }

    @JsonProperty("createTableDeckPlays")
    public boolean getCreateTableDeckPlays() { return createTableDeckPlays; }
    @JsonProperty("createTableDeckPlays")
    public void setCreateTableDeckPlays(boolean value) { this.createTableDeckPlays = value; }

    @JsonProperty("createTableEmptyClues")
    public boolean getCreateTableEmptyClues() { return createTableEmptyClues; }
    @JsonProperty("createTableEmptyClues")
    public void setCreateTableEmptyClues(boolean value) { this.createTableEmptyClues = value; }

    @JsonProperty("createTableCharacterAssignments")
    public boolean getCreateTableCharacterAssignments() { return createTableCharacterAssignments; }
    @JsonProperty("createTableCharacterAssignments")
    public void setCreateTableCharacterAssignments(boolean value) { this.createTableCharacterAssignments = value; }

    @JsonProperty("createTableAlertWaiters")
    public boolean getCreateTableAlertWaiters() { return createTableAlertWaiters; }
    @JsonProperty("createTableAlertWaiters")
    public void setCreateTableAlertWaiters(boolean value) { this.createTableAlertWaiters = value; }
}
