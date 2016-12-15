package com.example.tupac.myapplication.backend.models;

/**
 * Created by tupac on 12/3/2016.
 */

public class Competition {


    int competitionId;
    String competitionName;

    public Competition() {
        this.competitionId = 0;
        this.competitionName = "Not a real competition";
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }
}
