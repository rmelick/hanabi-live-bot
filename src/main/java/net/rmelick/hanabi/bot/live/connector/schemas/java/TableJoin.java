package net.rmelick.hanabi.bot.live.connector.schemas.java;

import com.fasterxml.jackson.annotation.*;

public class TableJoin {
    private long tableID;
    private String password;

    @JsonProperty("tableID")
    public long getTableID() { return tableID; }
    @JsonProperty("tableID")
    public void setTableID(long value) { this.tableID = value; }

    @JsonProperty("password")
    public String getPassword() { return password; }
    @JsonProperty("password")
    public void setPassword(String value) { this.password = value; }
}