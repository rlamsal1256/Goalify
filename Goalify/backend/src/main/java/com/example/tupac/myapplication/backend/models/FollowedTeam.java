package com.example.tupac.myapplication.backend.models;

/**
 * Created by tupac on 12/12/2016.
 */

public class FollowedTeam {

    String teamName;
    int teamId;

    public FollowedTeam(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public FollowedTeam() {

    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
