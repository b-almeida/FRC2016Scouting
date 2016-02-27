package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class NewMatchActivity extends AppCompatActivity {

    private static String LOG_TAG = "NewMatchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
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

    /**
     * @param id EditText view id.
     */
    private int getTeamNumberFromEditText(int id) {
        String teamNumberString = ( (EditText) findViewById(R.id.team_number) )
                .getText().toString();

        if (teamNumberString != null && teamNumberString.length() > 0) {
            return Integer.parseInt(teamNumberString);
        } else {
            return 0;
        }
    }

    public void createMatchOnClick(View view) {
        Log.v(LOG_TAG, "in createMatchOnClick()");

        int teamNumber = getTeamNumberFromEditText(R.id.team_number);
        int ally1TeamNumber = getTeamNumberFromEditText(R.id.ally_1_team_number);
        int ally2TeamNumber = getTeamNumberFromEditText(R.id.ally_2_team_number);
        int opponent1TeamNumber = getTeamNumberFromEditText(R.id.opponent_1_team_number);
        int opponent2TeamNumber = getTeamNumberFromEditText(R.id.opponent_2_team_number);
        int opponent3TeamNumber = getTeamNumberFromEditText(R.id.opponent_3_team_number);

        Match match = new Match(
                teamNumber,
                ally1TeamNumber,
                ally2TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);

        // Write the data to the database
        ProfileDBHelper.writeMatchToDB(this, match);

        // Switch to MatchActivity
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
        Log.v(LOG_TAG, "Starting MatchActivity");
    }

}
