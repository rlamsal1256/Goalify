package com.example.tupac.myapplication.backend.servlets;

import com.example.tupac.myapplication.backend.models.Competition;
import com.example.tupac.myapplication.backend.models.FollowedTeam;
import com.example.tupac.myapplication.backend.models.Match;
import com.example.tupac.myapplication.backend.models.Round;
import com.example.tupac.myapplication.backend.models.Team;
import com.example.tupac.myapplication.backend.service.ConvertJson;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Rupak Lamsal
 *
 * This is a servlet class that is the back end of the application. It will talk to the CrowdScores API, collect Json data,
 * parse them and make required model classes, for instance, teams, matches, etc. The model classes will then be stored in
 * the firebase database where the client app can access the info from. This servlet will also be used to push notification
 * to the client app using the Firebase Messaging.
 *
 */

public class MyServlet extends HttpServlet {
    static Logger Log = Logger.getLogger("com.example.tupac.myapplication.backend.servlets.MyServlet");

    private DatabaseReference firebase;

    ArrayList<Competition> competitionArrayList;
    ArrayList<Round> roundArrayList;
    ArrayList<Team> teamArrayList;
    ArrayList<Match> matchArrayList;
    ArrayList<FollowedTeam> followedTeamArrayList;


    FirebaseDatabase database;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String credential = config.getInitParameter("credential");
        String databaseURL = config.getInitParameter("databaseUrl");

        // Note: Ensure that the [PRIVATE_KEY_FILENAME].json has read
        // permissions set.
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(getServletContext().getResourceAsStream(credential))
                .setDatabaseUrl(databaseURL)
                .build();

        FirebaseApp.initializeApp(options);
        firebase = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

         /*
         * code used to create followed teams in firebase
         */
//        followedTeamArrayList=new ArrayList<>();
//        FollowedTeam ft = new FollowedTeam();
//        ft.setTeamId(7);
//        ft.setTeamName("Chelsea");
//        followedTeamArrayList.add(ft);
//        DatabaseReference followedTeamsRef = database.getReference().child("followedTeams");
//        Map<String, FollowedTeam> teamsMap = new HashMap<String, FollowedTeam>();
//        if(followedTeamArrayList!=null)
//        {
//            for (int i = 0; i < followedTeamArrayList.size(); i++) {
//                FollowedTeam team = followedTeamArrayList.get(i);
//                teamsMap.put(String.valueOf(team.getTeamId()), new FollowedTeam(team.getTeamId(), team.getTeamName()));
//            }
//        }
//
//        followedTeamsRef.setValue(teamsMap);


        /*
         * gets initiated if user follows a team; reacts by downloading games in the next week for that team.
         * stores that data in firebase database
         */
//        followedTeamArrayList = new ArrayList<>();
//        DatabaseReference followedTeamsRef = database.getReference("followedTeams");
//        followedTeamsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //getting data from snapshot
//                    FollowedTeam followedTeam = snapshot.getValue(FollowedTeam.class); //do get using this team name for the next week
//                    System.out.println("Followed team -> " + followedTeam.getTeamName());
//
//                    getMatchesFromApi(followedTeam.getTeamId());
//
//                    followedTeamArrayList.add(followedTeam);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        DatabaseReference ref = database.getReference();

        Map<String, Match> matchesMap = new HashMap<String, Match>();

        matchArrayList = new ArrayList<>();
        long addTime = 11; //sec
        Match match = new Match(100, "Test1",200, "Test2", 17, System.currentTimeMillis()/1000 + addTime);
        matchArrayList.add(match);
        if (matchArrayList != null) {
            for (int i=0; i<matchArrayList.size(); i++){
                Match m = matchArrayList.get(i);
                matchesMap.put(m.getHomeTeamName() + " vs " + match.getAwayTeamName(), m);
            }
        }

        ref.child("matches").setValue(matchesMap);

        DatabaseReference matchesRef = database.getReference("matches");

        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //getting data from snapshot
                    Match match = snapshot.getValue(Match.class);
                    System.out.println("Matches -> " + match.getStartTime());

                    long currentTime = System.currentTimeMillis();
                    //long fiveMins = 5 * 60; //in sec

                    long delayTime = match.getStartTime() * 1000 - currentTime;
                    System.out.println("CurrentTime: " + currentTime + "MatchTime: " + match.getStartTime() +
                        "delayTime: " + delayTime);

                    //Now create a new task with delayed execution
                    Queue queue = QueueFactory.getQueue("notification-queue");
                    queue.add(TaskOptions.Builder.withUrl("/notifications").param("teamName", String.valueOf(match.getAwayTeamName()))
                            .countdownMillis(delayTime));
//                    queue.add(TaskOptions.Builder.withUrl("/notifications").param("team", String.valueOf(match.getHomeTeamName()))
//                            .countdownMillis(delayTime));
                    System.out.println("Successfully created a Task in the Queue");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getMatchesFromApi(int teamId) {

        String matches = "/matches?team_id=" + teamId +
                "&round_ids=1044&competition_ids=2&from=2016-12-12T12:00:00-12:00&to=2016-12-20T12:00:00-12:00";

        // Making a request to url and getting response
        String mainUrl = "https://api.crowdscores.com/v1";

        HttpHandler sh = new HttpHandler();

        String matchesJsonStr = sh.makeServiceCall(mainUrl + matches);

        if (matchesJsonStr != null) {
            try {
                //resp.getWriter().println(matchesJsonStr);
                // Getting JSON Array node
                JSONArray matchlist = new JSONArray(matchesJsonStr);
                //use service class to parse this json array and create a match model
                matchArrayList = ConvertJson.getMatchfromJson(matchlist);

            } catch (final JSONException e) {

            }
        }

        if (matchArrayList != null) {
            for (int i=0; i<matchArrayList.size(); i++){

                System.out.println( matchArrayList.get(i).getMatchId() + "      "  +
                        matchArrayList.get(i).getHomeTeamName() + "     "  +
                        matchArrayList.get(i).getHomeGoals() + "    "  +
                        matchArrayList.get(i).getAwayGoals() + "    " +
                        matchArrayList.get(i).getAwayTeamName()  + "    " +
                        matchArrayList.get(i).getStartTime());
            }
        }


        DatabaseReference ref = database.getReference();

        Map<String, Match> matchesMap = new HashMap<String, Match>();

        if (matchArrayList != null) {
            for (int i=0; i<matchArrayList.size(); i++){
                Match match = matchArrayList.get(i);
                matchesMap.put(match.getHomeTeamName() + " vs " + match.getAwayTeamName(), new Match(match.getAwayGoals(), match.getAwayTeamName(),
                        match.getHomeGoals(), match.getHomeTeamName(), match.getMatchId(), match.getStartTime()));
            }
        }

        ref.child("matches").setValue(matchesMap);

    }





    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {


//        String outString;
//        outString = "<p>Match Id, Home Team, Away Team, Start Time</p>";
//
//        resp.getWriter().println(outString);
//
//        //doPost(req,resp);
//
//        /*
//         * use the following commented code only once to populate info in the firebase database
//         */
//
//        HttpHandler sh = new HttpHandler();
//        // Making a request to url and getting response
//        String mainUrl = "https://api.crowdscores.com/v1";
//        String competitions = "/competitions";
//        //competition_id for Premier League is 2
//
//        String rounds = "/rounds";
//        String teams = "/teams?round_ids=&competition_ids=2";
//        String matches = "/matches?team_id=7&round_ids=1044&competition_ids=2";
//        String chelseaMatches = "/matches?team_id=7&round_ids=1044&competition_ids=2&from=2016-12-12T12:00:00-12:00&to=2016-12-20T12:00:00-12:00";
//        String matchDetails = "/matches/69407";

//        String competitionsJsonStr = sh.makeServiceCall(mainUrl + competitions);
//
//        if (competitionsJsonStr != null) {
//            try {
//                //resp.getWriter().println(competitionsJsonStr);
//                // Getting JSON Array node
//                JSONArray competitionList = new JSONArray(competitionsJsonStr);
//                //use service class to parse this json array and create a competition model
//                competitionArrayList = ConvertJson.getCompetitionfromJson(competitionList);
//
//            } catch (final JSONException e) {
//            }
//        }
//
//        String roundsJsonStr = sh.makeServiceCall(mainUrl + rounds);
//
//        if (roundsJsonStr != null) {
//            try {
//                //resp.getWriter().println(roundsJsonStr);
//                // Getting JSON Array node
//                JSONArray roundList = new JSONArray(roundsJsonStr);
//                //use service class to parse this json array and create a round model
//                roundArrayList = ConvertJson.getRoundfromJson(roundList);
//
//            } catch (final JSONException e) {
//            }
//        }

//
//        String teamsJsonStr = sh.makeServiceCall(mainUrl + teams);
//
//        if (teamsJsonStr != null) {
//            try {
//                //resp.getWriter().println(teamsJsonStr);
//                // Getting JSON Array node
//                JSONArray teamList = new JSONArray(teamsJsonStr);
//                //use service class to parse this json array and create a team model
//                teamArrayList = ConvertJson.getTeamfromJson(teamList);
//
//            } catch (final JSONException e) {
//
//            }
//        }

//        String matchesJsonStr = sh.makeServiceCall(mainUrl + chelseaMatches);
//
//        if (matchesJsonStr != null) {
//            try {
//                //resp.getWriter().println(matchesJsonStr);
//                // Getting JSON Array node
//                JSONArray matchlist = new JSONArray(matchesJsonStr);
//                //use service class to parse this json array and create a match model
//                matchArrayList = ConvertJson.getMatchfromJson(matchlist);
//
//            } catch (final JSONException e) {
//
//            }
//        }

//        String matchDetailsJsonStr = sh.makeServiceCall(mainUrl + matchDetails);
//
//        if (matchDetailsJsonStr != null) {
//            try {
//                resp.getWriter().println(matchDetailsJsonStr);
//                // Getting JSON Array node
//                JSONArray matchlist = new JSONArray(matchDetailsJsonStr);
//                //use service class to parse this json array and create a match model
//                //matchArrayList = ConvertJson.getMatchfromJson(matchlist);
//
//            } catch (final JSONException e) {
//
//            }
//        }
//
//        if (competitionArrayList != null) {
//            for (int i=0; i<competitionArrayList.size(); i++){
//                resp.getWriter().println("<p>" +  competitionArrayList.get(i).getCompetitionId() + "&nbsp;&nbsp;&nbsp;" +
//                        competitionArrayList.get(i).getCompetitionName() + "<p>");
//
//            }
//        }
//
//        if (roundArrayList != null) {
//            for (int i=0; i<roundArrayList.size(); i++){
//                resp.getWriter().println("<p>" +  roundArrayList.get(i).getRoundId()  + "&nbsp;&nbsp;&nbsp;" +
//                        roundArrayList.get(i).getCompetition().getCompetitionId() + "&nbsp;&nbsp;&nbsp;" +
//                        roundArrayList.get(i).getCompetition().getCompetitionName() + "<p>");
//
//            }
//        }
//
//        if (teamArrayList != null) {
//            for (int i=0; i<teamArrayList.size(); i++){
//                resp.getWriter().println("<p>" +  teamArrayList.get(i).getTeamId() + "      "  +
//                        teamArrayList.get(i).getTeamName() + "      "  +
//                        teamArrayList.get(i).getTeamLocation().getTeamLatitude() + "      "  +
//                        teamArrayList.get(i).getTeamLocation().getTeamLatitude() + "<p>");
//            }
//        }
//
//        if (matchArrayList != null) {
//            for (int i=0; i<matchArrayList.size(); i++){
//
//                resp.getWriter().println("<p>" +  matchArrayList.get(i).getMatchId() + "&nbsp;&nbsp;&nbsp;"  +
//                        matchArrayList.get(i).getHomeTeamName() + "&nbsp;&nbsp;&nbsp;"  +
//                        matchArrayList.get(i).getHomeGoals() + "&nbsp;&nbsp;&nbsp;"  +
//                        matchArrayList.get(i).getAwayGoals() + "&nbsp;&nbsp;&nbsp;" +
//                        matchArrayList.get(i).getAwayTeamName()  + "&nbsp;&nbsp;&nbsp;" +
//                        matchArrayList.get(i).getStartTime() + "<p>");
//            }
//        }

//        DatabaseReference teamRef = database.getReference().child("teams");
//
//        Map<String, Team> teamsMap = new HashMap<String, Team>();
//
//        if (teamArrayList != null) {
//            for (int i=0; i<teamArrayList.size(); i++){
//                Team team = teamArrayList.get(i);
//                teamsMap.put(team.getTeamName(), new Team(team.getTeamId(), team.getTeamName(), team.getTeamLocation()));
//            }
//        }
//
//        teamRef.setValue(teamsMap);

         /*
         * use this only once to populate matches
         */
//        DatabaseReference matchesRef = database.getReference().child("matches");
//
//        Map<String, Match> matchesMap = new HashMap<String, Match>();
//
//        if (matchArrayList != null) {
//            for (int i=0; i<matchArrayList.size(); i++){
//                Match match = matchArrayList.get(i);
//                matchesMap.put(match.getHomeTeamName() + " vs " + match.getAwayTeamName(), new Match(match.getAwayGoals(), match.getAwayTeamName(),
//                        match.getHomeGoals(), match.getHomeTeamName(), match.getMatchId(), match.getStartTime()));
//            }
//        }
//
//        matchesRef.setValue(matchesMap);
//
//

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    }



    public class HttpHandler {

        public HttpHandler() {
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("x-crowdscores-api-key", "dc736bae447b4f6ba8ec68c530195151");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            } catch (MalformedURLException e) {
            } catch (ProtocolException e) {
            } catch (IOException e) {
            } catch (Exception e) {
            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

}
