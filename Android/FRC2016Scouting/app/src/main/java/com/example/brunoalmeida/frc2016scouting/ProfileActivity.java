package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    public static final String INTENT_PROFILE_ID = "profileID";

    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the team number from the intent
        long profileID = getIntent().getLongExtra(INTENT_PROFILE_ID, -1);
        Log.v(LOG_TAG, "profileID received from intent: " + profileID);

        // Read the robot type from the database
        profile = ProfileDBHelper.readProfile(this, profileID);
        Log.v(LOG_TAG, "profile received from intent:" + "\n" + profile);

        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team " + profile.getTeamNumber());
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, "Toolbar title: " + toolbar.getTitle().toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Match> matches = ProfileDBHelper.readMatches(this, profile.getTeamNumber());

        String log = "onCreate(): Matches read from database:\n";
        for (Match match : matches) {
            log += match + "\n";
        }
        Log.v(LOG_TAG, log);

        displayMatchList(matches);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void displayMatchList(final ArrayList<Match> matches) {
        // Create a string for each profile
        ArrayList<String> matchStrings = new ArrayList<>();
        for (Match match : matches) {
            matchStrings.add(getMatchString(match));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,                                   // The current context (this activity)
                android.R.layout.simple_list_item_1,    // The layout to populate.
                matchStrings);

        ListView matchList = (ListView) findViewById(R.id.match_list);
        matchList.setAdapter(arrayAdapter);
        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG, "match_list: In onItemClick()");
                Log.v(LOG_TAG, "view = " + view + ", position = " + position + ", id = " + id);

                startMatchActivity(matches.get(position).getID());
            }
        });
    }

    private String getMatchString(Match match) {
        String str = "";

        for (Team team : Team.values()) {
            str += match.getTeamNumber(team) + ", ";
        }

        if (str.endsWith(", ")) {
            str = str.substring(0, str.length() - 2);
        }

        return str;
    }

    public void newMatchOnClick(View view) {
        startNewMatchActivity();
    }

    private void startNewMatchActivity() {
        Intent intent = new Intent(this, NewMatchActivity.class);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting NewMatchActivity");
    }

    private void startMatchActivity(long matchID) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra(MatchActivity.INTENT_MATCH_ID, matchID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting MatchActivity");
    }

}
