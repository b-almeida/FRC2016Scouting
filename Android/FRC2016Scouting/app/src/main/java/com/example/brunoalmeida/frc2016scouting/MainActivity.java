package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this.deleteDatabase(ProfileDBHelper.DATABASE_NAME);
        //Log.v(LOG_TAG, "onCreate(): Database deleted");

        ArrayList<Profile> profiles = ProfileDBHelper.readAllProfiles(this);

        String log = "onCreate(): Profiles read from database:\n";
        for (Profile profile : profiles) {
            log += "teamNumber = " + profile.getTeamNumber() +
                    ", robotFunction = " + profile.getRobotFunction() + "\n";
        }
        Log.v(LOG_TAG, log);

        displayProfileList(profiles);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newProfileOnClick(View view) {
        Log.v(LOG_TAG, "In newProfileOnClick()");

        startNewProfileActivity();

        Log.v(LOG_TAG, "Starting NewProfileActivity");
    }

    private void displayProfileList(final ArrayList<Profile> profiles) {
        // Create a string for each profile
        ArrayList<String> profileStrings = new ArrayList<>();
        for (Profile profile : profiles) {
            profileStrings.add(String.valueOf(profile.getTeamNumber()));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,                                   // The current context (this activity)
                android.R.layout.simple_list_item_1,    // The layout to populate.
                profileStrings);

        ListView profileList = (ListView) findViewById(R.id.profile_list);
        profileList.setAdapter(arrayAdapter);
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG, "profile_list: In onItemClick()");
                Log.v(LOG_TAG, "view = " + view + ", position = " + position + ", id = " + id);
                startProfileActivity(profiles.get(position).getId());
            }
        });
    }

    private void startNewProfileActivity() {
        Intent intent = new Intent(this, NewProfileActivity.class);
        startActivity(intent);
    }

    private void startProfileActivity(long profileID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.INTENT_PROFILE_ID, profileID);
        startActivity(intent);
        Log.v(LOG_TAG, "Starting ProfileActivity");
    }

}
