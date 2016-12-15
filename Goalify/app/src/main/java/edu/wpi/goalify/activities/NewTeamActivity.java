package edu.wpi.goalify.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.wpi.goalify.R;
import edu.wpi.goalify.adapters.LeaguesAdapter;
import edu.wpi.goalify.adapters.TeamsAdapter;
import edu.wpi.goalify.dataservice.FirebaseUtil;
import edu.wpi.goalify.models.League;
import edu.wpi.goalify.models.Team;
import edu.wpi.goalify.models.TrackGPS;

public class NewTeamActivity extends AppCompatActivity {
    //region Constants
    public static final String LEAGUE_ID = "league_id";
    //endregion

    //region Private View Instances
    private EditText mTeamNameEditText;
    private Button mClearSearchButton;


    private LinearLayout mLocalsAndLeaguesLinearLayout;
    private ListView mLeaguesListView;
    private ListView mLocalTeamsListView;

    private LinearLayout mTeamsSearchResultLinearLayout;
    private TextView mTeamsSearchResultTitleTextView;
    private ListView mTeamsSearchResultListView;
    //endregion

    //region Private variables
    private TeamsAdapter mLocalTeamsAdapter;
    private TeamsAdapter mTeamsAdapter;
    private LeaguesAdapter mLeaguesAdapter;
    private ArrayList<Team> mAllTeamsList = new ArrayList<Team>();

    private TrackGPS gps;
    private double longitude;
    private double latitude;
    //endregion

    //region Activity Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        gps = new TrackGPS(NewTeamActivity.this);
        //current user location
//        if(gps.canGetLocation()) {
//            longitude = gps.getLongitude();
//            latitude = gps.getLatitude();
//        }
        //chelsea location
        latitude = 51.4851;
        longitude = -0.1749;

        setupActionBar();
        initControls();
        getLeagues();
        findLocalTeams();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    //region Event Handlers
    public void btnClearSearchOnClick(View view){
        if(mTeamsAdapter != null)
            mTeamsAdapter.clear();
        mTeamNameEditText.setText("");
    }
    //endregion

    //region Private Methods
    private void initControls(){
        mTeamNameEditText = (EditText) findViewById(R.id.editText_search_team_name);
        mTeamNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = editable.toString();
                if(content.length() <= 0){
                    goToSearchMode(false);
                }
                else {
                    goToSearchMode(true);
                    searchTeams(content);
                }
            }
        });
        mClearSearchButton = (Button) findViewById(R.id.btn_clearSearch);

        mLocalsAndLeaguesLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_locals_and_leagues);
        // Setting up the local teams list view
        mLocalTeamsListView = (ListView) findViewById(R.id.ListView_Local_Teams);
        mLocalTeamsAdapter = new TeamsAdapter(this, new ArrayList<Team>());
        mLocalTeamsListView.setAdapter(mLocalTeamsAdapter);
        mLocalTeamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent teamIntent = new Intent(NewTeamActivity.this, TeamDetailActivity.class);
                startActivity(teamIntent);
            }
        });

        // Setting up the leagues list view
        mLeaguesListView = (ListView) findViewById(R.id.ListView_Leagues);
        mLeaguesAdapter = new LeaguesAdapter(this, new ArrayList<League>());
        mLeaguesListView.setAdapter(mLeaguesAdapter);
        mLeaguesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent teamIntent = new Intent(NewTeamActivity.this, TeamsActivity.class);
                teamIntent.putExtra(LEAGUE_ID, 0L);
                startActivity(teamIntent);
            }
        });

        mTeamsSearchResultLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_teamsSearchResult);
        mTeamsSearchResultTitleTextView = (TextView) findViewById(R.id.TextView_teamsSearchResultTitle);
        // Setting up the teams search result listview
        mTeamsSearchResultListView = (ListView) findViewById(R.id.ListView_teamsResult);
        mTeamsAdapter = new TeamsAdapter(this, new ArrayList<Team>());
        mTeamsSearchResultListView.setAdapter(mTeamsAdapter);
        mTeamsSearchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Team team = (Team)parent.getItemAtPosition(position);
                String name = team.getTeamName();
                goToTeamDetailActivity(name);
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.add_new_team);
    }

    private void goToTeamDetailActivity(String teamName){
        Intent teamDetailIntent = new Intent(this, TeamDetailActivity.class);
        teamDetailIntent.putExtra(MainActivity.TEAM_NAME, teamName);
        startActivity(teamDetailIntent);
    }

    private void goToSearchMode(boolean b){
        if(b){
            mClearSearchButton.setVisibility(View.VISIBLE);
            mLocalsAndLeaguesLinearLayout.setVisibility(View.GONE);
            mTeamsSearchResultLinearLayout.setVisibility(View.VISIBLE);
        }
        else{
            mLocalsAndLeaguesLinearLayout.setVisibility(View.VISIBLE);
            mTeamsSearchResultLinearLayout.setVisibility(View.GONE);
            mClearSearchButton.setVisibility(View.GONE);
        }
    }

    //region Data Methods
    private void getLeagues(){
        mLeaguesAdapter.clear();

        // TODO: Update to get leagues from firebase instead
        // Getting leagues
        League epl = new League("EPL");
        mLeaguesAdapter.add(epl);
        mLeaguesAdapter.notifyDataSetChanged();
    }

    /**
     * Submits a request to firebase to get a list of teams
     * @param teamName The name of the team to search for.
     */
    private void searchTeams(String teamName){
        mTeamsAdapter.clear();
        Query queryReference = FirebaseUtil.getTeamsReference().orderByKey().startAt(teamName).endAt(teamName + "\uf8ff");
        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapShots = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : snapShots) {
                    Team team = snapshot.getValue(Team.class);
                    mTeamsAdapter.add(team);
                }
                mTeamsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mTeamsSearchResultTitleTextView.setText(getString(R.string.text_search_result_for) + ": " + teamName);

    }

    /**
     * Submits a request to firebase to get a list of all teams
     *
     */
    private void findLocalTeams(){
        mAllTeamsList.clear();
        Query queryReference = FirebaseUtil.getTeamsReference().orderByKey();
        queryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapShots = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : snapShots) {
                    Team team = snapshot.getValue(Team.class);
                    mAllTeamsList.add(team);

                }
                getLocalTeams();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mTeamsSearchResultTitleTextView.setText(getString(R.string.text_search_result_for) + ": " + teamName);

    }


    private void getLocalTeams(){

        int count = 0;
        int distance = 100;
        mLocalTeamsAdapter.clear();

        for(int i=0 ; i<mAllTeamsList.size() ; i++){
            Team team = mAllTeamsList.get(i);
            if(calculateDistanceInKilometer(team.getTeamLocation().getTeamLatitude(),team.getTeamLocation().getTeamLongitude(),
                                            latitude,longitude) < distance && count < 4) {
                mLocalTeamsAdapter.add(team);
                count++;
            }
        }
        mLocalTeamsAdapter.notifyDataSetChanged();

    }

    /**
     * Calculate distance between two lat/long points
     * @param userLat
     * @param userLng
     * @param venueLat
     * @param venueLng
     * @return distance in kilometers
     */
    public int calculateDistanceInKilometer(double userLat, double userLng,
                                            double venueLat, double venueLng) {
        double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
    }
    //endregion
    //endregion
}
