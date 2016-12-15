package edu.wpi.goalify.complexmodels;

/**
 * @author Jules Voltaire on 12/5/2016. (Generated)
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.wpi.goalify.complexmodels.helpers.Geolocation;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "name",
        "dbid",
        "geolocation",
        "isNational",
        "shortName"
})
public class Team {
    @JsonProperty("name")
    private String name;
    @JsonProperty("dbid")
    private int dbid;
    @JsonProperty("geolocation")
    private Geolocation geolocation;
    @JsonProperty("isNational")
    private boolean isNational;
    @JsonProperty("shortName")
    private String shortName;

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The dbid
     */
    @JsonProperty("dbid")
    public int getDbid() {
        return dbid;
    }

    /**
     *
     * @param dbid
     * The dbid
     */
    @JsonProperty("dbid")
    public void setDbid(int dbid) {
        this.dbid = dbid;
    }

    /**
     *
     * @return
     * The geolocation
     */
    @JsonProperty("geolocation")
    public Geolocation getGeolocation() {
        return geolocation;
    }

    /**
     *
     * @param geolocation
     * The geolocation
     */
    @JsonProperty("geolocation")
    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    /**
     *
     * @return
     * The isNational
     */
    @JsonProperty("isNational")
    public boolean isIsNational() {
        return isNational;
    }

    /**
     *
     * @param isNational
     * The isNational
     */
    @JsonProperty("isNational")
    public void setIsNational(boolean isNational) {
        this.isNational = isNational;
    }

    /**
     *
     * @return
     * The shortName
     */
    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    /**
     *
     * @param shortName
     * The shortName
     */
    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
