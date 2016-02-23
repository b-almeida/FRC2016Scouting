package com.example.brunoalmeida.frc2016scouting;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.brunoalmeida.frc2016scouting.database.ProfileContract;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;

public class NewProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NewProfileActivity";

    public static final String INTENT_TEAM_NUMBER = "teamNumber";
    public static final String INTENT_ROBOT_TYPE = "robotType";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public void createProfileOnClick(View view) {
        // Get the data from the interface
        String teamNumberString = ( (EditText) findViewById(R.id.team_number) )
                .getText().toString();

        int teamNumber;
        if (teamNumberString != null && teamNumberString.length() > 0) {
            teamNumber = Integer.parseInt(teamNumberString);
        } else {
            teamNumber = 0;
        }

        String robotType = ( (Spinner) findViewById(R.id.robot_type) )
                .getSelectedItem().toString();

        // Write the data to the database
        writeProfileToDB(teamNumber, robotType);

        // Switch to NewProfileActivity
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(INTENT_TEAM_NUMBER, teamNumber);
        //intent.putExtra(INTENT_ROBOT_TYPE, robotType);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting ProfileActivity");
    }

    private void writeProfileToDB(int teamNumber, String robotType) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProfileEntry.COLUMN_TEAM_NUMBER, teamNumber);
        values.put(ProfileEntry.COLUMN_ROBOT_TYPE, robotType);

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeProfileToDB(): newRowID = " + newRowID);
    }

}
