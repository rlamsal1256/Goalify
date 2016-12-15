package com.example.tupac.myapplication.backend.service;

import com.example.tupac.myapplication.backend.models.Competition;
import com.example.tupac.myapplication.backend.models.Match;
import com.example.tupac.myapplication.backend.models.Round;
import com.example.tupac.myapplication.backend.models.Team;
import com.example.tupac.myapplication.backend.models.TeamLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tupac on 12/4/2016.
 */

public class ConvertJson {


    static ArrayList<Competition> competitionArrayList;
    static ArrayList<Round> roundArrayList;
    static ArrayList<Team> teamArrayList;
    static ArrayList<Match> matchArrayList;

    /*
     * Decodes competition json into competition model object
     *
     * @param   jsonObject  a json object that represents a competition
     * @return              a competition with its attributes
     */
    public static Competition getCompetitionfromJson(JSONObject jsonObject) {
        Competition competition = new Competition();
        // Deserialize json into object fields
        try {
            competition.setCompetitionName(jsonObject.getString("name"));
            competition.setCompetitionId(jsonObject.getInt("dbid"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return competition;
    }

    /*
    * Decodes array of competitions json results into airlines model objects
    *
    * @param   jsonArray  a json array that represents a list of airlines
    * @return             array list of competition
    */
    public static ArrayList<Competition> getCompetitionfromJson(JSONArray jsonArray) {
        JSONObject competitionsJSON;

        competitionArrayList = new ArrayList<Competition>(jsonArray.length());

        // Process each result in json array, decode and convert to competition object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                competitionsJSON = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Competition competition = getCompetitionfromJson(competitionsJSON);
            if (competition != null) {
                competitionArrayList.add(competition);
            }
        }
        return competitionArrayList;
    }

      /*
   * Decodes array of rounds json results into airlines model objects
   *
   * @param   jsonArray  a json array that represents a list of airlines
   * @return             array list of rounds
   */

    public static ArrayList<Round> getRoundfromJson(JSONArray roundList) {
        JSONObject roundJSON;

        roundArrayList = new ArrayList<Round>(roundList.length());

        // Process each result in json array, decode and convert to round object
        for (int i = 0; i < roundList.length(); i++) {
            try {
                roundJSON = roundList.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Round round = getRoundfromJson(roundJSON);
            if (round != null) {
                roundArrayList.add(round);
            }
        }
        return roundArrayList;
    }

    /*
    * Decodes rounds json into rounds model object
    *
    * @param   jsonObject  a json object that represents a round
    * @return              a round with its attributes
    */

    private static Round getRoundfromJson(JSONObject roundJSON) {
        Round round = new Round();
        // Deserialize json into object fields
        try {
            round.setRoundId(roundJSON.getInt("dbid"));
            round.setCompetition(getCompetitionfromJson(roundJSON.getJSONObject("competition")));

        } catch (JSONException e) {
            return round;
        }
        // Return new object
        return round;
    }


    /*
    * Decodes array of teams json results into team model objects
    *
    * @param   jsonArray  a json array that represents a list of teams
    * @return             array list of teams
    */
    public static ArrayList<Team> getTeamfromJson(JSONArray teamList) {

        JSONObject teamsJSON;

        teamArrayList = new ArrayList<Team>(teamList.length());

        // Process each result in json array, decode and convert to team object
        for (int i = 0; i < teamList.length(); i++) {
            try {
                teamsJSON = teamList.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Team team = getTeamfromJson(teamsJSON);
            if (team != null) {
                teamArrayList.add(team);
            }
        }
        return teamArrayList;
    }

    /*
   * Decodes teams json into teams model object
   *
   * @param   jsonObject  a json object that represents a team
   * @return              a team with its attributes
   */
    private static Team getTeamfromJson(JSONObject teamsJSON) {
        Team team = new Team();
        // Deserialize json into object fields
        try {
            team.setTeamId(teamsJSON.getInt("dbid"));
            team.setTeamName(teamsJSON.getString("name"));

            JSONObject defaultHomeVenue = teamsJSON.getJSONObject("defaultHomeVenue");
            team.setTeamLocation(getGeoLocationfromJson(defaultHomeVenue));

        } catch (JSONException e) {
            return team;
        }
        // Return new object
        return team;
    }

    private static TeamLocation getGeoLocationfromJson(JSONObject defaultHomeVenue) {
        TeamLocation teamLocation = new TeamLocation();

        // Deserialize json into object fields
        try {
            JSONObject geolocation = defaultHomeVenue.getJSONObject("geolocation");
            teamLocation.setTeamLatitude(geolocation.getDouble("latitude"));
            teamLocation.setTeamLongitude(geolocation.getDouble("longitude"));


        } catch (JSONException e) {
            return teamLocation;
        }
        // Return new object
        return teamLocation;

    }


    /*
   * Decodes array of matches json results into match model objects
   *
   * @param   jsonArray  a json array that represents a list of matches
   * @return             array list of matches
   */
    public static ArrayList<Match> getMatchfromJson(JSONArray matchlist) {

        JSONObject matchJSON;

        matchArrayList = new ArrayList<Match>(matchlist.length());

        // Process each result in json array, decode and convert to match object
        for (int i = 0; i < matchlist.length(); i++) {
            try {
                matchJSON = matchlist.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Match match = getMatchfromJson(matchJSON);
            if (match != null) {
                matchArrayList.add(match);
            }
        }
        return matchArrayList;
    }

    /*
    * Decodes match json into match model object
    *
    * @param   jsonObject  a json object that represents a match
    * @return              a match with its attributes
    */
    private static Match getMatchfromJson(JSONObject matchJSON) {

        Match match = new Match();
        // Deserialize json into object fields
        try {
            match.setMatchId(matchJSON.getInt("dbid"));
            match.setHomeTeamName(getTeamfromJson(matchJSON.getJSONObject("homeTeam")).getTeamName());
            match.setAwayTeamName(getTeamfromJson(matchJSON.getJSONObject("awayTeam")).getTeamName());
            match.setHomeGoals(matchJSON.getInt("homeGoals"));
            match.setAwayGoals(matchJSON.getInt("awayGoals"));
            match.setStartTime(matchJSON.getLong("start"));

//            Double start = matchJSON.getDouble("start");
//            String startStr = new BigDecimal(start).toPlainString();
//            Date startDate = new Date(Long.parseLong(startStr));
//            match.setStartTime(startDate);

        } catch (JSONException e) {
            return match;
        }
        // Return new object
        return match;
    }
}

