package edu.wpi.goalify.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.wpi.goalify.R;
import edu.wpi.goalify.adapters.TeamsAdapter;
import edu.wpi.goalify.dataservice.FirebaseUtil;
import edu.wpi.goalify.models.Team;
import edu.wpi.goalify.models.TeamLocation;
import edu.wpi.goalify.sqlite.DBHelper;

public class TeamsActivity extends AppCompatActivity {

    //region Constants
    private final long LEAGUE_ID_DEFAULT = -1000000000000001L;
    //endregion

    //region View Instances
    private ListView mTeamsListView;
    //endregion

    //region Private Variables
    private TeamsAdapter mTeamsAdapter;
    private ChildEventListener leageTeamsListener;
    //endregion

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        setupActionBar();

        // Setting up the Teams ListView
        mTeamsListView = (ListView)findViewById(R.id.ListView_TeamsList);
        mTeamsAdapter = new TeamsAdapter(this, new ArrayList<Team>());
        mTeamsListView.setAdapter(mTeamsAdapter);
        mTeamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team team = (Team)parent.getItemAtPosition(position);
                String name = team.getTeamName();
                goToTeamDetailActivity(name);
            }
        });

        // Grab data
        long leagueId = getIntent().getLongExtra(NewTeamActivity.LEAGUE_ID, LEAGUE_ID_DEFAULT);
        if(leagueId == LEAGUE_ID_DEFAULT){
            //TODO: Show users teams
            getFollowedTeams();
            setTitle("Your Teams");
        }
        else{
            //TODO: use the teamID to grab teams but for now
            setTitle("EPL");
            getTeamsForLeague(leagueId);
        }
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

    @Override
    protected void onStop() {
        if(leageTeamsListener != null)
            FirebaseUtil.getTeamsReference().removeEventListener(leageTeamsListener);
        super.onStop();
    }

    //endregion

    //region Private Methods
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.my_teams);
    }

    private void goToTeamDetailActivity(String teamName){
        Intent teamDetailIntent = new Intent(this, TeamDetailActivity.class);
        teamDetailIntent.putExtra(MainActivity.TEAM_NAME, teamName);
        startActivity(teamDetailIntent);
    }
    //endregion

    //region DataMethods
    private void getTeamsForLeague(long leageId){
        Query query = FirebaseUtil.getTeamsReference().orderByKey();
        leageTeamsListener = query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Team team = dataSnapshot.getValue(Team.class);
                mTeamsAdapter.add(team);
                mTeamsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowedTeams(){
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAllTeams();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int teamID = cursor.getInt(0);
            String teamName = cursor.getString(1);
            double lat = cursor.getDouble(2);
            double lon = cursor.getDouble(3);

            Team t = new Team(teamID, teamName, new TeamLocation(lat, lon));
            mTeamsAdapter.add(t);

            cursor.moveToNext();
        }

        mTeamsAdapter.notifyDataSetChanged();
        cursor.close();

    }
    //endregion
}
