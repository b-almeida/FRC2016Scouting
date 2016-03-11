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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Statistic;
import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.data.SuccessRate;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.Stack;

public class MatchActivity extends AppCompatActivity {

    public static final String INTENT_MATCH_ID = "matchID";
    private static final String LOG_TAG = "MatchActivity";
    private Menu menu;

    private Match match;
    private Stack<EnumMap.Entry<Statistic, SuccessRate>> undoActions = new Stack<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the match ID  from the intent
        long matchID = getIntent().getLongExtra(INTENT_MATCH_ID, -1);
        Log.v(LOG_TAG, "matchID received from intent: " + matchID);

        // Read the match from the database
        match = ProfileDBHelper.readMatch(this, matchID);
        Log.v(LOG_TAG, "match received from intent:" + match);


        // Set the title bar to the team number
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(match.getDescription() + " - Team " + match.getTeamNumber(Team.ALLY_1));
        actionBar.setDisplayShowTitleEnabled(true);
        Log.v(LOG_TAG, "Toolbar title: " + toolbar.getTitle().toString());


        // Populate the list view using a custom adapter
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

        ProfileDBHelper.updateMatch(this, match);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        updateUndoActionMenuItem();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.action_undo) {
            undoAction();
            return true;
        }

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

    private void updateUndoActionMenuItem() {
        Log.v(LOG_TAG, "In updateUndoActionMenuItem()");

        MenuItem undoActionMenuItem = menu.findItem(R.id.action_undo);

        if (undoActions.size() > 0) {
            undoActionMenuItem.setEnabled(true);
            undoActionMenuItem.getIcon().setAlpha(255);
        } else {
            undoActionMenuItem.setEnabled(false);
            undoActionMenuItem.getIcon().setAlpha(0);
        }
    }

    private void pushUndoAction(EnumMap.Entry<Statistic, SuccessRate> rate) {
        undoActions.push(rate);
        updateUndoActionMenuItem();
    }

    private EnumMap.Entry<Statistic, SuccessRate> popUndoAction() {
        EnumMap.Entry<Statistic, SuccessRate> rate = undoActions.pop();
        updateUndoActionMenuItem();
        return rate;
    }

    private void undoAction() {
        if (undoActions.size() > 0) {
            EnumMap.Entry<Statistic, SuccessRate> action = popUndoAction();

            Statistic statistic = action.getKey();
            SuccessRate successRate = action.getValue();

            match.setStatistic(statistic, successRate);

            ListView statList = (ListView) findViewById(R.id.stat_list);
            ((MatchBaseAdapter) statList.getAdapter()).notifyDataSetChanged();

        } else {
            Log.v(LOG_TAG, "In undoAction(): no undo actions found in stack");
        }
    }


    private class MatchBaseAdapter extends BaseAdapter {

        private static final String LOG_TAG = "MatchBaseAdapter";

        private Match match;


        public MatchBaseAdapter(Match match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            return match.getStatistics().size();
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
            if (position < Statistic.values().length) {
                return Statistic.values()[position].getDisplayString();
            } else {
                Log.w(LOG_TAG, "getDescription(): Statistic not found");
                return "Not Found";
            }
        }

        private SuccessRate getSuccessRate(int position) {
            if (position < match.getStatistics().size()) {
                return match.getStatistic(position);
            } else {
                Log.w(LOG_TAG, "getSuccessRate(): Statistic not found");
                return new SuccessRate();
            }
        }

        private void successOnClick(int position) {
            if (position < match.getStatistics().size()) {
                Statistic statistic = Statistic.values()[position];

                EnumMap.Entry<Statistic, SuccessRate> oldSuccessRate =
                        new AbstractMap.SimpleEntry(statistic, match.getStatistic(statistic));
                pushUndoAction(oldSuccessRate);

                Log.v(LOG_TAG, "In successOnClick(): " + undoActions.size() + " undo actions in stack");

                match.setStatistic(position,
                        oldSuccessRate.getValue().getSuccesses() + 1,
                        oldSuccessRate.getValue().getAttempts() + 1);

            } else {
                Log.w(LOG_TAG, "successOnClick(): Statistic not found");
            }

            notifyDataSetChanged();
        }

        private void missOnClick(int position) {
            if (position < match.getStatistics().size()) {
                Statistic statistic = Statistic.values()[position];

                EnumMap.Entry<Statistic, SuccessRate> oldSuccessRate =
                        new AbstractMap.SimpleEntry(statistic, match.getStatistic(statistic));
                pushUndoAction(oldSuccessRate);

                Log.v(LOG_TAG, "In missOnClick(): " + undoActions.size() + " undo actions in stack");

                match.setStatistic(position,
                        oldSuccessRate.getValue().getSuccesses(),
                        oldSuccessRate.getValue().getAttempts() + 1);

            } else {
                Log.w(LOG_TAG, "In missOnClick(): Statistic not found");
            }

            notifyDataSetChanged();
        }

    }

}
