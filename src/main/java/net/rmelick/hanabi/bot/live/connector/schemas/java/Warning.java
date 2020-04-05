package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Warning {
    private String warning;

    @JsonProperty("warning")
    public String getWarning() { return warning; }
    @JsonProperty("warning")
    public void setWarning(String value) { this.warning = value; }
}