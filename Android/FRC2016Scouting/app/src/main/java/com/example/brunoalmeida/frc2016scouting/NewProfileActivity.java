package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class NewProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NewProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        String robotFunction = ( (Spinner) findViewById(R.id.robot_function) )
                .getSelectedItem().toString();

        // Write to the database
        long profileID = ProfileDBHelper.writeProfile(this, new Profile(teamNumber, robotFunction));

        startProfileActivity(profileID);
    }

    private void startProfileActivity(long profileID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.INTENT_PROFILE_ID, profileID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting ProfileActivity");
    }

}
