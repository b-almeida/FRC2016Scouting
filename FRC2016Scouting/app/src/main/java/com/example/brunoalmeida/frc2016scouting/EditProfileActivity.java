package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    private static String[] dataRobotFunctions = {
            "Unknown",
            "Defensive",
            "Breacher",
            "Bellhop",
            "Low Shooter",
            "High Shooter",
            "General Shooter",
            "Hybrid Offensive",
            "Pure Hybrid"
    };

    private static ArrayList<String> robotFunctions = new ArrayList<>();

    static {
        for (String function : dataRobotFunctions) {
            robotFunctions.add(function);
        }
    }




    private static final String LOG_TAG = "NewProfileActivity";

    public static final String INTENT_PROFILE_ID = "ProfileID";


    private static long profileID = -1;

    private Profile profile;
    private ArrayList<Profile> existingProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the team number from the intent
        long id = getIntent().getLongExtra(INTENT_PROFILE_ID, -1);
        if (id != -1) {
            profileID = id;
        }

        Log.v(LOG_TAG, "profileID received from intent: " + profileID);

        // Read the robot type from the database
        profile = ProfileDBHelper.readProfile(this, profileID);
        Log.v(LOG_TAG, "profile received from intent: " + profile);

        existingProfiles = ProfileDBHelper.readAllProfiles(this);




/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    protected void onStart() {
        super.onStart();

        EditText teamNumberInput = (EditText) findViewById(R.id.team_number);
        EditText descriptionInput = (EditText) findViewById(R.id.description);
        Spinner robotFunctionInput = (Spinner) findViewById(R.id.robot_function);
        EditText notesInput = (EditText) findViewById(R.id.notes);

        teamNumberInput.setText(String.valueOf(profile.getTeamNumber()));
        descriptionInput.setText(profile.getDescription());
        robotFunctionInput.setSelection(robotFunctions.indexOf(profile.getRobotFunction()));
        notesInput.setText(profile.getNotes());

        Button createButton = (Button) findViewById(R.id.create_profile);
        createButton.setText("Save");
    }

    private boolean doesProfileExist(Profile newProfile) {
        for (Profile existingProfile : existingProfiles) {
            if (newProfile.getTeamNumber() == existingProfile.getTeamNumber()) {
                return true;
            }
        }

        return false;
    }

    public void createProfileOnClick(View view) {
        boolean allDataValid = true;

        /* Get the data from the interface */

        // Team number
        EditText teamNumberInput = (EditText) findViewById(R.id.team_number);
        String teamNumberString = teamNumberInput.getText().toString().trim();

        if (teamNumberString.isEmpty()) {
            allDataValid = false;
            teamNumberInput.setError("Can't be empty.");

        } else if (Integer.parseInt(teamNumberString) < 0) {
            allDataValid = false;
            teamNumberInput.setError("Can't be negative.");
        }

        if (allDataValid) {
            int teamNumber = Integer.parseInt(teamNumberString);


            // Description
            EditText descriptionInput = (EditText) findViewById(R.id.description);
            String description = descriptionInput.getText().toString().trim();


            // Robot function
            Spinner robotFunctionInput = (Spinner) findViewById(R.id.robot_function);
            String robotFunction = robotFunctionInput.getSelectedItem().toString();


            // Notes
            EditText notesInput = (EditText) findViewById(R.id.notes);
            String notes = notesInput.getText().toString().trim().replace("\n", "; ");


            // Update profile
            Profile profile = new Profile(this.profile.getID(), teamNumber, description, robotFunction, notes);

            if (profile.getTeamNumber() != this.profile.getTeamNumber() && doesProfileExist(profile)) {
                teamNumberInput.setError("Team " + teamNumber + " already exists.");
            } else {
                ProfileDBHelper.updateProfile(this, profile);
                startProfileActivity(profileID);
            }
        }
    }

    private void startProfileActivity(long profileID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.INTENT_PROFILE_ID, profileID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting ProfileActivity");
    }

}
