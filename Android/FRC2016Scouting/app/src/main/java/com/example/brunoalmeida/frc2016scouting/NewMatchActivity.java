package com.example.brunoalmeida.frc2016scouting;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.brunoalmeida.frc2016scouting.database.ProfileContract;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.regex.Matcher;

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
        writeMatchToDB(match);

        // Switch to MatchActivity
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
        Log.v(LOG_TAG, "Starting MatchActivity");
    }

    private void writeMatchToDB(Match match) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MatchEntry.COLUMN_TEAM_NUMBER, match.getTeamNumber());
        values.put(MatchEntry.COLUMN_ALLY_1_TEAM_NUMBER, match.getAlly1TeamNumber());
        values.put(MatchEntry.COLUMN_ALLY_2_TEAM_NUMBER, match.getAlly2TeamNumber());
        values.put(MatchEntry.COLUMN_OPPONENT_1_TEAM_NUMBER, match.getOpponent1TeamNumber());
        values.put(MatchEntry.COLUMN_OPPONENT_2_TEAM_NUMBER, match.getOpponent2TeamNumber());
        values.put(MatchEntry.COLUMN_OPPONENT_3_TEAM_NUMBER, match.getOpponent3TeamNumber());

        values.put(MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES, match.getLowShootingSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS, match.getLowShootingSuccess().getDenominator());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES, match.getHighShootingSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS, match.getHighShootingSuccess().getDenominator());

        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES, match.getDefenseLowBarBreachSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS, match.getDefenseLowBarBreachSuccess().getDenominator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES, match.getDefenseCategoryABreachSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS, match.getDefenseCategoryABreachSuccess().getDenominator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES, match.getDefenseCategoryBBreachSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS, match.getDefenseCategoryBBreachSuccess().getDenominator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES, match.getDefenseCategoryCBreachSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS, match.getDefenseCategoryCBreachSuccess().getDenominator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES, match.getDefenseCategoryDBreachSuccess().getNumerator());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS, match.getDefenseCategoryDBreachSuccess().getDenominator());

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileContract.MatchEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeMatchToDB(): newRowID = " + newRowID);
    }

}
