package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

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
        profile = ProfileDBHelper.readProfileFromDB(this, profileID);

        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team " + profile.getTeamNumber());
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, "Toolbar title: " + toolbar.getTitle().toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void newMatchOnClick(View view) {
        // Switch to NewMatchActivity
        Intent intent = new Intent(this, NewMatchActivity.class);
        startActivity(intent);
        Log.v(LOG_TAG, "Starting NewMatchActivity");
    }

}
