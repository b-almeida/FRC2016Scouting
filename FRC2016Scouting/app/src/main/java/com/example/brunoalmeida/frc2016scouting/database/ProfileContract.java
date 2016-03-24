package com.example.brunoalmeida.frc2016scouting.database;

import android.provider.BaseColumns;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Statistic;

import java.util.EnumMap;

/**
 * Created by Bruno on 2016-02-22.
 */
public abstract class ProfileContract {

    public static class ColumnPair {

        private String successes;
        private String attempts;

        public ColumnPair(String successes, String attempts) {
            this.successes = successes;
            this.attempts = attempts;
        }

        public String getSuccesses() {
            return successes;
        }

        public String getAttempts() {
            return attempts;
        }

    }


    public static abstract class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "Profiles";

        public static final String COLUMN_TEAM_NUMBER = "TeamNumber";
        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_ROBOT_FUNCTION = "RobotFunction";
        public static final String COLUMN_NOTES = "Notes";
    }


    public static abstract class MatchEntry implements BaseColumns {

        public static final String TABLE_NAME = "Matches";


        public static final String COLUMN_DESCRIPTION = "Description";
        public static final String COLUMN_NOTES = "Notes";
        public static final EnumMap<Team, String> COLUMNS_TEAM_NUMBERS =
                new EnumMap<>(Team.class);
        public static final EnumMap<Statistic, ColumnPair> COLUMNS_STATISTICS =
                new EnumMap<>(Statistic.class);


        // Construct COLUMNS_TEAM_NUMBERS, COLUMNS_STATISTICS
        static {
            for (Team team : Team.values()) {
                COLUMNS_TEAM_NUMBERS.put(team, "TEAM_NUMBER_" + team.toString());
            }

            for (Statistic statistic : Statistic.values()) {
                String baseString = statistic.getDatabaseColumnString();

                COLUMNS_STATISTICS.put(statistic,
                        new ColumnPair(baseString + "_Successes", baseString + "_Attempts"));
            }
        }

    }

}
