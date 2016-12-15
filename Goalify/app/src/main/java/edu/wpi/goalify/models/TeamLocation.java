package edu.wpi.goalify.models;

/**
 * Created by tupac and Jules Voltaire on 12/4/2016.
 */

public class TeamLocation {
    double teamLatitude;
    double teamLongitude;

    public TeamLocation(){}

    public TeamLocation(double lat, double lon) {
        this.teamLatitude = lat;
        this.teamLongitude = lon;
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
