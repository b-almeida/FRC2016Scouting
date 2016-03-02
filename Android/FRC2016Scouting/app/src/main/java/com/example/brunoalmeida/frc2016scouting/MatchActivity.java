package com.example.brunoalmeida.frc2016scouting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Shooting;
import com.example.brunoalmeida.frc2016scouting.data.Match.DefenseBreach;
import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.data.SuccessRate;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

public class MatchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MatchActivity";

    public static final String INTENT_MATCH_ID = "matchID";

    private Match match;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the match ID  from the intent
        long matchID = getIntent().getLongExtra(INTENT_MATCH_ID, -1);
        Log.v(LOG_TAG, "matchID received from intent: " + matchID);

        // Read the robot type from the database
        match = ProfileDBHelper.readMatch(this, matchID);
        Log.v(LOG_TAG, "match received from intent:" + "\n" + match);

        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Team " + match.getTeamNumber(Team.ALLY_1) + " Match");
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, "Toolbar title: " + toolbar.getTitle().toString());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView statList = (ListView) findViewById(R.id.stat_list);
        statList.setAdapter(new MatchBaseAdapter(match));

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
    protected void onPause() {
        Log.v(LOG_TAG, "In onPause()");

        super.onPause();

        ProfileDBHelper.writeMatch(this, match);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startProfileActivity();
        }
        return true;
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

        Log.v(LOG_TAG, "Starting ProfileActivity");
    }




    private class MatchBaseAdapter extends BaseAdapter {

        private static final String LOG_TAG = "MatchBaseAdapter";

        private Match match;


        public MatchBaseAdapter(Match match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            Log.v(LOG_TAG, "In getCount()");
            return match.getShootingRates().size() + match.getDefenseBreachRates().size();
        }

        @Override
        public Object getItem(int position) {
            Log.v(LOG_TAG, "In getItem()");
            return null;
        }

        @Override
        public long getItemId(int position) {
            Log.v(LOG_TAG, "In getItemId()");
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Log.v(LOG_TAG, "In getView(): convertView = null");

                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.match_row, parent, false);
            }

            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            TextView successRate = (TextView) convertView.findViewById(R.id.success_rate);
            Button successButton = (Button) convertView.findViewById(R.id.success);
            Button missButton = (Button) convertView.findViewById(R.id.miss);

            description.setText(getDescription(position));
            successRate.setText(getSuccessRate(position).toString());
            //successRate.setText(new SuccessRate(position + 1, position + 1).toString());

            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(LOG_TAG, "In getView(): layout.onClick()");
                }
            });

            successButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(LOG_TAG, "In getView(): successButton.onClick()");
                    successOnClick(position);
                }
            });

            missButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.v(LOG_TAG, "In getView(): missButton.onClick()");
                    missOnClick(position);
                }
            });

            return convertView;
        }

        private String getDescription(int position) {
            if (position < match.getShootingRates().size()) {
                int shootingIndex = position;
                return Shooting.values()[shootingIndex].getDisplayString();

            } else if (position < match.getShootingRates().size() + match.getDefenseBreachRates().size()) {
                int defenseBreachIndex = position - match.getShootingRates().size();
                return DefenseBreach.values()[defenseBreachIndex].getDisplayString();

            } else {
                Log.w(LOG_TAG, "getSuccessRate(): SuccessRate not found in match");
                return "Not Found";
            }
        }

        private SuccessRate getSuccessRate(int position) {
            if (position < match.getShootingRates().size()) {
                int shootingIndex = position;
                return match.getShootingRate(shootingIndex);

            } else if (position < match.getShootingRates().size() + match.getDefenseBreachRates().size()) {
                int defenseBreachIndex = position - match.getShootingRates().size();
                return match.getDefenseBreachRate(defenseBreachIndex);

            } else {
                Log.w(LOG_TAG, "getSuccessRate(): SuccessRate not found in match");
                return new SuccessRate();
            }
        }

        private void successOnClick(int position) {
            SuccessRate currentRate = getSuccessRate(position);

            if (position < match.getShootingRates().size()) {
                int shootingIndex = position;
                match.setShootingRate(shootingIndex,
                        currentRate.getSuccesses() + 1, currentRate.getAttempts() + 1);

            } else if (position < match.getShootingRates().size() + match.getDefenseBreachRates().size()) {
                int defenseBreachIndex = position - match.getShootingRates().size();
                match.setDefenseBreachRate(defenseBreachIndex,
                        currentRate.getSuccesses() + 1, currentRate.getAttempts() + 1);

            } else {
                Log.w(LOG_TAG, "successOnClick(): SuccessRate not found in match");
            }

            notifyDataSetChanged();
        }

        private void missOnClick(int position) {
            SuccessRate currentRate = getSuccessRate(position);

            if (position < match.getShootingRates().size()) {
                int shootingIndex = position;
                match.setShootingRate(shootingIndex,
                        currentRate.getSuccesses(), currentRate.getAttempts() + 1);

            } else if (position < match.getShootingRates().size() + match.getDefenseBreachRates().size()) {
                int defenseBreachIndex = position - match.getShootingRates().size();
                match.setDefenseBreachRate(defenseBreachIndex,
                        currentRate.getSuccesses(), currentRate.getAttempts() + 1);

            } else {
                Log.w(LOG_TAG, "missOnClick(): SuccessRate not found in match");
            }

            notifyDataSetChanged();
        }

    }

}
