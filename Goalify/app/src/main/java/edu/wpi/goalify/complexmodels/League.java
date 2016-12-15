package edu.wpi.goalify.complexmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Jules Voltaire on 12/5/2016. (Generated)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "name",
        "dbid",
        "region",
        "fullName",
        "shortName"
})
public class League {

    @JsonProperty("name")
    private String name;
    @JsonProperty("dbid")
    private int dbid;
    @JsonProperty("region")
    private String region;
    @JsonProperty("fullName")
    private String fullName;
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
     * The region
     */
    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    /**
     *
     * @param region
     * The region
     */
    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     *
     * @return
     * The fullName
     */
    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     * The fullName
     */
    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
