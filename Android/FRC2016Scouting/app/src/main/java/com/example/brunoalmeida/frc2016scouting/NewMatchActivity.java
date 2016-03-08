package com.example.brunoalmeida.frc2016scouting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class NewMatchActivity extends AppCompatActivity {

    private static String LOG_TAG = "NewMatchActivity";

    public static final String INTENT_TEAM_NUMBER = "TeamNumber";

    private int teamNumber = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the team number from the intent
        int teamNumber = getIntent().getIntExtra(INTENT_TEAM_NUMBER, -1);
        if (teamNumber != -1) {
            this.teamNumber = teamNumber;
        }

        Log.v(LOG_TAG, "teamNumber received from intent: " + this.teamNumber);

        ((TextView) findViewById(R.id.ally_1_team_number)).setText("Team " + teamNumber);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    /**
     * @param editTextID EditText view id.
     */
    private int getTeamNumberFromEditText(int editTextID) {
        String teamNumberString = ((EditText) findViewById(editTextID))
                .getText().toString().trim();

        if (teamNumberString.length() > 0) {
            return Integer.parseInt(teamNumberString);
        } else {
            return 0;
        }
    }

    public void createMatchOnClick(View view) {
        Log.v(LOG_TAG, "in createMatchOnClick()");

        boolean allDataValid = true;

        EditText descriptionInput = (EditText) findViewById(R.id.description);
        if (descriptionInput.getText().toString().trim().isEmpty()) {
            allDataValid = false;
            descriptionInput.setError("Can't be blank.");
        }

        for (int editTextID : new int[] {
                R.id.ally_2_team_number,
                R.id.ally_3_team_number,
                R.id.opponent_1_team_number,
                R.id.opponent_2_team_number,
                R.id.opponent_3_team_number} ) {

            EditText editText = (EditText) findViewById(editTextID);
            if (editText.getText().toString().trim().isEmpty()) {
                allDataValid = false;
                editText.setError("Can't be blank.");
            }
        }

        if (allDataValid) {
            String description = descriptionInput.getText().toString().trim();

            int ally1TeamNumber = teamNumber;
            int ally2TeamNumber = getTeamNumberFromEditText(R.id.ally_2_team_number);
            int ally3TeamNumber = getTeamNumberFromEditText(R.id.ally_3_team_number);
            int opponent1TeamNumber = getTeamNumberFromEditText(R.id.opponent_1_team_number);
            int opponent2TeamNumber = getTeamNumberFromEditText(R.id.opponent_2_team_number);
            int opponent3TeamNumber = getTeamNumberFromEditText(R.id.opponent_3_team_number);

            Match match = new Match(
                    description,
                    ally1TeamNumber,
                    ally2TeamNumber,
                    ally3TeamNumber,
                    opponent1TeamNumber,
                    opponent2TeamNumber,
                    opponent3TeamNumber);

            long matchID = ProfileDBHelper.writeMatch(this, match);

            startMatchActivity(matchID);
        }
    }

    private void startMatchActivity(long matchID) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra(MatchActivity.INTENT_MATCH_ID, matchID);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting MatchActivity");
    }

}
