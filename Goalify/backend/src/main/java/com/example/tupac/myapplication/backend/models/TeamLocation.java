package com.example.tupac.myapplication.backend.models;

/**
 * Created by tupac on 12/4/2016.
 */

public class TeamLocation {
    double teamLatitude;
    double teamLongitude;

    public TeamLocation() {
        this.teamLatitude = 0.0;
        this.teamLongitude = 0.0;
    }

    public double getTeamLatitude() {
        return teamLatitude;
    }

    public void setTeamLatitude(double teamLatitude) {
        this.teamLatitude = teamLatitude;
    }

    public double getTeamLongitude() {
        return teamLongitude;
    }

    public void setTeamLongitude(double teamLongitude) {
        this.teamLongitude = teamLongitude;
    }
}
