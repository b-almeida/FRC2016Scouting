package com.example.brunoalmeida.frc2016scouting.database;

import android.provider.BaseColumns;

/**
 * Created by Bruno on 2016-02-22.
 */
public abstract class ProfileContract {

    public static abstract class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "profiles";

        public static final String COLUMN_TEAM_NUMBER = "teamNumber";
        public static final String COLUMN_ROBOT_TYPE = "robotType";
    }

    public static abstract class MatchEntry implements BaseColumns {
        public static final String TABLE_NAME = "matches";

        public static final String COLUMN_TEAM_NUMBER               = "teamNumber";
        public static final String COLUMN_ALLY_1_TEAM_NUMBER        = "ally1TeamNumber";
        public static final String COLUMN_ALLY_2_TEAM_NUMBER        = "ally2TeamNumber";
        public static final String COLUMN_OPPONENT_1_TEAM_NUMBER    = "opponent1TeamNumber";
        public static final String COLUMN_OPPONENT_2_TEAM_NUMBER    = "opponent2TeamNumber";
        public static final String COLUMN_OPPONENT_3_TEAM_NUMBER    = "opponent3TeamNumber";

        public static final String COLUMN_LOW_SHOOTING_SUCCESSES    = "lowShootingSuccesses";
        public static final String COLUMN_LOW_SHOOTING_ATTEMPTS     = "lowShootingAttempts";
        public static final String COLUMN_HIGH_SHOOTING_SUCCESSES   = "highShootingSuccesses";
        public static final String COLUMN_HIGH_SHOOTING_ATTEMPTS    = "highShootingAttempts";

        public static final String COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES      = "defenseLowBarBreachSuccesses";
        public static final String COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS       = "defenseLowBarBreachAttempts";
        public static final String COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES   = "defenseCategoryABreachSuccesses";
        public static final String COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS    = "defenseCategoryABreachAttempts";
        public static final String COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES   = "defenseCategoryBBreachSuccesses";
        public static final String COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS    = "defenseCategoryBBreachAttempts";
        public static final String COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES   = "defenseCategoryCBreachSuccesses";
        public static final String COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS    = "defenseCategoryCBreachAttempts";
        public static final String COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES   = "defenseCategoryDBreachSuccesses";
        public static final String COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS    = "defenseCategoryDBreachAttempts";
    }

}
