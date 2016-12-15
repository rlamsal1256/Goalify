package edu.wpi.goalify.models;

/**
 * @author Jules Voltaire on 12/12/2016.
 */

public class League {
    //region Private Variables
    private String mLeagueName;
    //endregion

    //region Constructor/s
    public League(String leagueName){
        this.mLeagueName = leagueName;
    }
    //endregion

    //region Getters
    public String getLeagueName() {
        return mLeagueName;
    }
    //endregion

    //region Setters
    public void setLeagueName(String mLeagueName) {
        this.mLeagueName = mLeagueName;
    }
    //endregion
}
