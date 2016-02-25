package com.example.brunoalmeida.frc2016scouting;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.brunoalmeida.frc2016scouting.database.ProfileContract;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        ArrayList<Profile> profiles = readProfilesFromDB();

        String log = "onCreate(): Profiles read from database:\n";
        for (Profile profile : profiles) {
            log += "teamNumber = " + profile.getTeamNumber() +
                    ", robotFunction = " + profile.getRobotFunction() + "\n";

        }
        Log.v(LOG_TAG, log);
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

        // Switch to NewProfileActivity
        Intent intent = new Intent(this, NewProfileActivity.class);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting NewProfileActivity");
    }

    private ArrayList<Profile> readProfilesFromDB() {
        ArrayList<Profile> profiles = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(this);
        SQLiteDatabase db = profileDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileEntry.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            while (! cursor.isAfterLast()) {
                int teamNumber = cursor.getInt(
                        cursor.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_TEAM_NUMBER));
                String robotType = cursor.getString(
                        cursor.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_ROBOT_TYPE));

                Log.v(LOG_TAG, "readProfilesFromDB(): teamNumber = " + teamNumber +
                        ", robotFunction = " + robotType);

                profiles.add(new Profile(teamNumber, robotType));
                cursor.moveToNext();
            }
        }

        return profiles;
    }

    private void displayProfileList(ArrayList<Profile> profiles) {

    }

}
