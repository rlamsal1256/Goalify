package edu.wpi.goalify.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wpi.goalify.R;
import edu.wpi.goalify.models.FollowedTeam;
import edu.wpi.goalify.models.Team;
import edu.wpi.goalify.models.TeamLocation;
import edu.wpi.goalify.sqlite.DBHelper;

/**
 * @author Jules Voltaire on 12/11/2016.
 */

public class TeamsAdapter extends ArrayAdapter<Team> {

    //list of followed teams
    private static ArrayList<Team> followedTeamArrayList;

    private DBHelper dbHelper;

    //region Constructor/s
    /**
     * Initializes a new instance of this class
     * @param context
     * @param teams
     */
    public TeamsAdapter(Context context, ArrayList<Team> teams){
        super(context, R.layout.item_team, teams);
        init();
    }

    private void init() {
        dbHelper = new DBHelper(getContext());
        followedTeamArrayList = new ArrayList<>();

        Cursor cursor = dbHelper.getAllTeams();
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int teamID = cursor.getInt(0);
            String teamName = cursor.getString(1);
            double lat = cursor.getDouble(2);
            double lon = cursor.getDouble(3);

            Team t = new Team(teamID, teamName, new TeamLocation(lat, lon));
            followedTeamArrayList.add(t);

            cursor.moveToNext();
        }
        cursor.close();

//        printList();
//        deleteList();
        printList();


    }

    private void printList() {
        if (followedTeamArrayList != null){
            for (Team team: followedTeamArrayList){
                System.out.println("printling listtttttttttttt" + team.getTeamName());
            }
        }
    }

    private void deleteList() {
        if (followedTeamArrayList != null){
            for (Team team: followedTeamArrayList){
                System.out.println("deleting listtttttttttttt" + team.getTeamName());
                dbHelper.deleteTeam(team.getTeamId());
            }
        }

    }

    //region Overridden Methods

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Grab the data item for this position, make sure that it is not null
        final Team team = getItem(position);

        if(team == null)
            return super.getView(position, convertView, parent);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_team, parent, false);
        }

        // Grabbing the widgets that need to be updated with data
        TextView tvTeamName = (TextView)convertView.findViewById(R.id.item_teamName_textVIew);

        // Setting the new data
        tvTeamName.setText(team.getTeamName());

        //toggle button
        final ToggleButton followTeamBtn = (ToggleButton) convertView.findViewById(R.id.item_followTeam_toggleButton);

        setupFollowButton(followTeamBtn, team);

        followTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFav = false;
                int index = -1;

                //check if team is a favorite
                if (followedTeamArrayList != null){
                    isFav = doesContain(team);
                }

                if (!isFav){  //team is not a favorite

                    //System.out.println("Team not a favorite but should be now");

                    //make team a favorite
                    followedTeamArrayList.add(team);


                    FirebaseMessaging.getInstance().subscribeToTopic(team.getTeamName());
                    System.out.println("Subscribed to " + team.getTeamName());

                    DatabaseReference followedTeamsRef = FirebaseDatabase.getInstance().getReference().child("followedTeams");
                    Map<String, FollowedTeam> teamsMap = new HashMap<>();
                    if(followedTeamArrayList!=null)
                    {
                        for (int i = 0; i < followedTeamArrayList.size(); i++) {
                            Team team = followedTeamArrayList.get(i);
                            teamsMap.put(String.valueOf(team.getTeamId()), new FollowedTeam(team.getTeamId(), team.getTeamName()));
                        }
                    }

                    followedTeamsRef.setValue(teamsMap);

                    followTeamBtn.setChecked(true);

                } else { //team is a favorite

                    //System.out.println("Team a favorite but shouldn't be now");

                    //un-favorite team

                    dbHelper.deleteTeam(team.getTeamId());

                    removeTeam(team.getTeamId());


                    FirebaseMessaging.getInstance().unsubscribeFromTopic(team.getTeamName());
                    System.out.println("Unsubscribed from " + team.getTeamName());

                    FirebaseDatabase.getInstance().
                            getReference().child("followedTeams")
                            .child(String.valueOf(team.getTeamId()))
                            .removeValue();


                    followTeamBtn.setChecked(false);



                }

                //save the array list
                if (followedTeamArrayList != null){
                    for (Team t: followedTeamArrayList){
                        dbHelper.insertTeam(t.getTeamId(), t.getTeamName(), t.getTeamLocation().getTeamLatitude(), t.getTeamLocation().getTeamLongitude());
                    }
                }


            }
        });

        // Return the updated team item view
        return convertView;
    }

    private void removeTeam(int id) {
        Iterator<Team> it = followedTeamArrayList.iterator();
        while (it.hasNext()) {
            if (it.next().getTeamId() == id) {
                it.remove();


                System.out.println(" ...............deleted...........");
            }
        }


    }


    private void setupFollowButton(ToggleButton followTeamBtn,  Team team) {

        boolean isFav = false;

        //check if the team is a favorite
        if (followedTeamArrayList != null){

            isFav = doesContain(team);
        }

        if (!isFav){  //team is not a favorite

            //System.out.println("**************is not a favorite team*********************");

            //un-fill favorite image
            followTeamBtn.setChecked(false);
        } else { //team is a favorite


            System.out.println("**************is a favorite team*********************");
            //fill the favorite image
            followTeamBtn.setChecked(true);

        }
    }

    private boolean doesContain(Team team) {
        for (Team t: followedTeamArrayList){
            if (t.getTeamName().equals(team.getTeamName())){
                return true;
            }
        }
        return false;
    }
    //endregion
}
