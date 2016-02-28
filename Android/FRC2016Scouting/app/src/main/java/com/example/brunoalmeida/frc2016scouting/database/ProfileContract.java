package com.example.brunoalmeida.frc2016scouting.database;

import android.provider.BaseColumns;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Shooting;
import com.example.brunoalmeida.frc2016scouting.data.Match.DefenseBreach;

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
        public static final String TABLE_NAME = "profiles";

        public static final String COLUMN_TEAM_NUMBER = "teamNumber";
        public static final String COLUMN_ROBOT_FUNCTION = "robotFunction";
    }


    public static abstract class MatchEntry implements BaseColumns {

        public static final String TABLE_NAME = "matches";

        public static final EnumMap<Team, String> TEAM_NUMBER_COLUMNS =
                new EnumMap<>(Team.class);
        public static final EnumMap<Shooting, ColumnPair> SHOOTING_RATE_COLUMNS =
                new EnumMap<>(Shooting.class);
        public static final EnumMap<DefenseBreach, ColumnPair> DEFENSE_BREACH_RATE_COLUMNS =
                new EnumMap<>(DefenseBreach.class);


        static {
            for (Team team : Team.values()) {
                TEAM_NUMBER_COLUMNS.put(team, "TEAM_NUMBER_" + team.toString());
            }

            for (Shooting shooting : Shooting.values()) {
                String baseString = "SHOOTING_RATE_" + shooting.toString();

                SHOOTING_RATE_COLUMNS.put(shooting,
                        new ColumnPair(baseString + "_SUCCESSES", baseString + "_ATTEMPTS"));
            }

            for (DefenseBreach defenseBreach : DefenseBreach.values()) {
                String baseString = "DEFENSE_BREACH_RATE_" + defenseBreach.toString();

                DEFENSE_BREACH_RATE_COLUMNS.put(defenseBreach,
                        new ColumnPair(baseString + "_SUCCESSES", baseString + "_ATTEMPTS"));
            }
        }

    }

}
