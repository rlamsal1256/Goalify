package com.example.tupac.myapplication.backend.models;

/**
 * Created by tupac on 12/4/2016.
 */

public class Round {
    int roundId;
    Competition competition;

    public Round() {
        Competition competition = new Competition();
        this.competition = competition;
        this.roundId = 0;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }
}
