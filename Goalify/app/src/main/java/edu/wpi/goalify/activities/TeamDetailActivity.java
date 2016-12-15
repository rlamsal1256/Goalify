package edu.wpi.goalify.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.wpi.goalify.R;
import edu.wpi.goalify.adapters.MatchesAdapter;
import edu.wpi.goalify.adapters.TeamsAdapter;
import edu.wpi.goalify.dataservice.FirebaseUtil;
import edu.wpi.goalify.models.Match;
import edu.wpi.goalify.models.Team;

public class TeamDetailActivity extends AppCompatActivity {
    //region View Instances
    private ListView mMatchesListView;
    private TextView mNoResultTextView;
    //endregion

    //region Private Variables
    private MatchesAdapter mMatchesAdapter;
    private ChildEventListener teamsMatchesListener;
    //endregion

    //region Overridden Activity Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        setupActionBar();

        mNoResultTextView = (TextView)findViewById(R.id.TextView_NoMatches);

        // Setting up the Teams ListView
        mMatchesListView = (ListView)findViewById(R.id.ListView_TeamMatches);
        mMatchesAdapter = new MatchesAdapter(this, new ArrayList<Match>());
        mMatchesListView.setAdapter(mMatchesAdapter);

        // Grab data
        String teamName= getIntent().getStringExtra(MainActivity.TEAM_NAME);
        if(teamName == null){
            setTitle("No Result");
        }
        else{
            //TODO: use the teamID to grab teams but for now
            setTitle(teamName);
            getMatchesForTeam(teamName);
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
        if(teamsMatchesListener != null)
            FirebaseUtil.getTeamsReference().removeEventListener(teamsMatchesListener);
        super.onStop();
    }
    //endregion

    //region Private Helper Methods
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Teams");
    }

    private void showResult(boolean b){
        if(b){
            mNoResultTextView.setVisibility(View.GONE);
            mMatchesListView.setVisibility(View.VISIBLE);
        }
        else{
            mNoResultTextView.setVisibility(View.VISIBLE);
            mMatchesListView.setVisibility(View.GONE);
        }
    }

    private void getMatchesForTeam(final String teamName){
        Query query = FirebaseUtil.getMatchesReference().orderByChild("startTime");
        teamsMatchesListener = query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Match match = dataSnapshot.getValue(Match.class);
                if(match.getAwayTeamName().equals(teamName) || match.getHomeTeamName().equals(teamName)){
                    mMatchesAdapter.add(match);
                    mMatchesAdapter.notifyDataSetChanged();
                    if(mMatchesAdapter.getCount() <= 0)
                        showResult(false);
                    else
                        showResult(true);
                }
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
    //endregion


}
