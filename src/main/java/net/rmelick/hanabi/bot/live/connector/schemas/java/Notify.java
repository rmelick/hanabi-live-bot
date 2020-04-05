package net.rmelick.hanabi.bot.live.connector.schemas.java;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Notify {
    private String type;
    private Long who;
    private Long rank;
    private Long suit;
    private Long order;
    private Long clues;
    private Long score;
    private Long maxScore;
    private Boolean doubleDiscard;
    private String text;
    private Long num;
    private Clue clue;
    private Long giver;
    private List<Long> list;
    private Long target;
    private Long turn;

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("who")
    public Long getWho() { return who; }
    @JsonProperty("who")
    public void setWho(Long value) { this.who = value; }

    @JsonProperty("rank")
    public Long getRank() { return rank; }
    @JsonProperty("rank")
    public void setRank(Long value) { this.rank = value; }

    @JsonProperty("suit")
    public Long getSuit() { return suit; }
    @JsonProperty("suit")
    public void setSuit(Long value) { this.suit = value; }

    @JsonProperty("order")
    public Long getOrder() { return order; }
    @JsonProperty("order")
    public void setOrder(Long value) { this.order = value; }

    @JsonProperty("clues")
    public Long getClues() { return clues; }
    @JsonProperty("clues")
    public void setClues(Long value) { this.clues = value; }

    @JsonProperty("score")
    public Long getScore() { return score; }
    @JsonProperty("score")
    public void setScore(Long value) { this.score = value; }

    @JsonProperty("maxScore")
    public Long getMaxScore() { return maxScore; }
    @JsonProperty("maxScore")
    public void setMaxScore(Long value) { this.maxScore = value; }

    @JsonProperty("doubleDiscard")
    public Boolean getDoubleDiscard() { return doubleDiscard; }
    @JsonProperty("doubleDiscard")
    public void setDoubleDiscard(Boolean value) { this.doubleDiscard = value; }

    @JsonProperty("text")
    public String getText() { return text; }
    @JsonProperty("text")
    public void setText(String value) { this.text = value; }

    @JsonProperty("num")
    public Long getNum() { return num; }
    @JsonProperty("num")
    public void setNum(Long value) { this.num = value; }

    @JsonProperty("clue")
    public Clue getClue() { return clue; }
    @JsonProperty("clue")
    public void setClue(Clue value) { this.clue = value; }

    @JsonProperty("giver")
    public Long getGiver() { return giver; }
    @JsonProperty("giver")
    public void setGiver(Long value) { this.giver = value; }

    @JsonProperty("list")
    public List<Long> getList() { return list; }
    @JsonProperty("list")
    public void setList(List<Long> value) { this.list = value; }

    @JsonProperty("target")
    public Long getTarget() { return target; }
    @JsonProperty("target")
    public void setTarget(Long value) { this.target = value; }

    @JsonProperty("turn")
    public Long getTurn() { return turn; }
    @JsonProperty("turn")
    public void setTurn(Long value) { this.turn = value; }
}