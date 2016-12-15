package com.example.tupac.myapplication.backend.models;

import java.util.Date;

/**
 * Created by tupac on 12/4/2016.
 */

public class Match {

    int matchId;
    String homeTeamName;
    String awayTeamName;
    int homeGoals;
    int awayGoals;
    long startTime;



    public Match() {   }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Match(int awayGoals, String awayTeamName, int homeGoals, String homeTeamName, int matchId, long startTime) {
        this.matchId = matchId;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.startTime = startTime;
    }

}
