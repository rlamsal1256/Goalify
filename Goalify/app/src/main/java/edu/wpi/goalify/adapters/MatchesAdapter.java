package edu.wpi.goalify.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.wpi.goalify.R;
import edu.wpi.goalify.models.Match;

/**
 * @author Jules Voltaire on 12/13/2016.
 */

public class MatchesAdapter extends ArrayAdapter<Match> {

    public MatchesAdapter(Context context, ArrayList<Match> matches) {
        super(context, R.layout.item_match, matches);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Grab the data item for this position, make sure that it is not null
        Match match = getItem(position);
//        if(match == null)
//            return super.getView(position, convertView, parent);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_match, parent, false);
        }

        // Grabbing the widgets that need to be updated with data
        TextView homeTeamTextView = (TextView)convertView.findViewById(R.id.TextView_homeTeamName);
        TextView scoresTeamTextView = (TextView)convertView.findViewById(R.id.TextView_matchScores);
        TextView awayTeamTextView = (TextView)convertView.findViewById(R.id.TextView_awayTeamName);

        // Setting the new data
        homeTeamTextView.setText(match.getHomeTeamName());
        scoresTeamTextView.setText(formatScores(match.getHomeGoals(), match.getAwayGoals()));
        awayTeamTextView.setText(match.getAwayTeamName());

        // Return the updated team item view
        return convertView;
    }

    //region Private Methods
    private String formatScores(int homeScore, int awayScore){
        return String.format("%d - %d", homeScore, awayScore);
    }
    //endregion
}
