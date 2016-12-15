package edu.wpi.goalify.models;


/**
 * Created by tupac and Jules Voltaire on 12/3/2016.
 */

public class Team {
    int teamId;
    String teamName;
    TeamLocation teamLocation;

    public Team() {
    }

    public Team(int teamId, String teamName, TeamLocation teamLocation) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLocation = teamLocation;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public TeamLocation getTeamLocation() {
        return teamLocation;
    }

    public void setTeamLocation(TeamLocation teamLocation) {
        this.teamLocation = teamLocation;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
