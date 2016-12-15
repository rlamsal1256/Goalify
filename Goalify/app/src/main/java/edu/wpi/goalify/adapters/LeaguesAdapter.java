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
import edu.wpi.goalify.models.League;
import edu.wpi.goalify.models.Team;

/**
 * @author Jules Voltaire on 12/12/2016.
 */

public class LeaguesAdapter extends ArrayAdapter<League>{
    //region Constructor/s
    /**
     * Initializes a new instance of this class
     * @param context
     * @param leagues
     */
    public LeaguesAdapter(Context context, ArrayList<League> leagues){
        super(context, R.layout.item_team, leagues);
    }
    //endregion

    //region Overridden Methods

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Grab the data item for this position, make sure that it is not null
        League league = getItem(position);
//        if(league == null)
//            return super.getView(position, convertView, parent);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_league, parent, false);
        }

        // Grabbing the widgets that need to be updated with data
        TextView tvLeagueName = (TextView)convertView.findViewById(R.id.item_leagueName_textVIew);

        // Setting the new data
        tvLeagueName.setText(league.getLeagueName());

        // Return the updated team item view
        return convertView;
    }
    //endregion
}
