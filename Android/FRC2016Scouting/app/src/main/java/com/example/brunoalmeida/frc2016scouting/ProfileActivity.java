package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brunoalmeida.frc2016scouting.database.ProfileContract;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;

import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    private int teamNumber = 0;
    private String robotType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the team number from the intent
        teamNumber = getIntent().getIntExtra(NewProfileActivity.INTENT_TEAM_NUMBER, 0);
        Log.v(LOG_TAG, "teamNumber received from intent: " + teamNumber);

        // Read the robot type from the database
        robotType = readRobotTypeFromDB(teamNumber);

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

    private String readRobotTypeFromDB(int teamNumber) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(this);

        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProfileEntry.COLUMN_ROBOT_TYPE
        };

        String selection = ProfileEntry.TABLE_NAME +
                "." + ProfileEntry.COLUMN_TEAM_NUMBER + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(teamNumber)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProfileEntry.COLUMN_ROBOT_TYPE + " DESC";

        Cursor cursor = db.query(
                ProfileEntry.TABLE_NAME,    // The table to query
                projection,                 // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,              // The values for the WHERE clause
                null,                       // don't group the rows
                null,                       // don't filter by row groups
                sortOrder                   // The sort order
        );

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_ROBOT_TYPE);
        String robotType = cursor.getString(columnIndex);

        Log.v(LOG_TAG, "readRobotTypeFromDB(): robotType = " + robotType);

        return robotType;
    }

}
