package com.example.brunoalmeida.frc2016scouting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    public static final String INTENT_PROFILE_ID = "profileID";
    private static long profileID = -1;

    private Profile profile;
    private ArrayList<Match> matches;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Get the team number from the intent
        long id = getIntent().getLongExtra(INTENT_PROFILE_ID, -1);
        if (id != -1) {
            profileID = id;
        }

        Log.v(LOG_TAG, "profileID received from intent: " + profileID);

        // Read the robot type from the database
        profile = ProfileDBHelper.readProfile(this, profileID);
        Log.v(LOG_TAG, "profile received from intent:" + "\n" + profile);


        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team " + profile.getTeamNumber() + " - Matches");
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, "Toolbar title: " + toolbar.getTitle().toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        matches = ProfileDBHelper.readMatches(this, profile.getTeamNumber());

        String log = "onCreate(): Matches read from database:\n";
        for (Match match : matches) {
            log += match + "\n";
        }
        Log.v(LOG_TAG, log);

        ListView matchList = (ListView) findViewById(R.id.match_list);
        matchList.setAdapter(new ProfileBaseAdapter(matches));


/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startMainActivity();
        }

        return true;
    }

    public void newMatchOnClick(View view) {
        startNewMatchActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting MainActivity");
    }

    private void startNewMatchActivity() {
        Intent intent = new Intent(this, NewMatchActivity.class);
        intent.putExtra(NewMatchActivity.INTENT_TEAM_NUMBER, profile.getTeamNumber());
        startActivity(intent);

        Log.v(LOG_TAG, "Starting NewMatchActivity");
    }

    private void startMatchActivity(long matchID) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra(MatchActivity.INTENT_MATCH_ID, matchID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting MatchActivity");
    }




    private class ProfileBaseAdapter extends BaseAdapter {

        private static final String LOG_TAG = "ProfileBaseAdapter";

        private ArrayList<Match> matches;




        public ProfileBaseAdapter(ArrayList<Match> matches) {
            this.matches = matches;
        }

        @Override
        public int getCount() {
            Log.v(LOG_TAG, "In getCount()");
            return matches.size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(LOG_TAG, "In getItem()");
            return null;
        }

        @Override
        public long getItemId(int position) {
            Log.v(LOG_TAG, "In getItemId()");
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Log.v(LOG_TAG, "In getView(): convertView = null");

                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.profile_row, parent, false);
            }


            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            TextView primaryString = (TextView) convertView.findViewById(R.id.primary_string);
            TextView secondaryString = (TextView) convertView.findViewById(R.id.secondary_string);

            Match match = matches.get(position);


            primaryString.setText(match.getDescription());

            secondaryString.setText(String.format("Teams %s, %s, %s vs. Teams %s, %s, %s",
                    match.getTeamNumber(Team.ALLY_1),
                    match.getTeamNumber(Team.ALLY_2),
                    match.getTeamNumber(Team.ALLY_3),
                    match.getTeamNumber(Team.OPPONENT_1),
                    match.getTeamNumber(Team.OPPONENT_2),
                    match.getTeamNumber(Team.OPPONENT_3)));

            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(LOG_TAG, "In getView(): layout.onClick()");
                    Log.v(LOG_TAG, "position = " + position + ", v = " + v);

                    startMatchActivity(matches.get(position).getID());
                }
            });

            return convertView;
        }

    }

}
