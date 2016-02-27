package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    public static final String INTENT_TEAM_NUMBER = "teamNumber";
    public static final String INTENT_ROBOT_TYPE = "robotType";

    private int teamNumber = 0;
    private String robotType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the team number from the intent
        teamNumber = getIntent().getIntExtra(INTENT_TEAM_NUMBER, 0);
        Log.v(LOG_TAG, "teamNumber received from intent: " + teamNumber);

        // Read the robot type from the database
        robotType = ProfileDBHelper.readRobotTypeFromDB(this, teamNumber);

        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team " + teamNumber);
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, toolbar.getTitle().toString());

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void newMatchOnClick(View view) {
        // Switch to NewMatchActivity
        Intent intent = new Intent(this, NewMatchActivity.class);
        startActivity(intent);
        Log.v(LOG_TAG, "Starting NewMatchActivity");
    }

}
