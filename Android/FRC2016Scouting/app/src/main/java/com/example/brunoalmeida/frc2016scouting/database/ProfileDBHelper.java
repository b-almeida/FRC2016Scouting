package com.example.brunoalmeida.frc2016scouting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;

import com.example.brunoalmeida.frc2016scouting.Profile;

/**
 * Created by Bruno on 2016-02-22.
 */
public class ProfileDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "Profiles.db";
    private static final int DATABASE_VERSION = 1;


    // Profile Table - SQL operations
    private static final String SQL_CREATE_PROFILE_TABLE =
            "CREATE TABLE " + ProfileEntry.TABLE_NAME +
                    " (" +
                    ProfileEntry._ID                + " INTEGER PRIMARY KEY," +
                    ProfileEntry.COLUMN_TEAM_NUMBER + " INTEGER," +
                    ProfileEntry.COLUMN_ROBOT_TYPE  + " TEXT" +
                    ")";

    private static final String SQL_DROP_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME;


    // Match Table - SQL operations
    private static final String SQL_CREATE_MATCH_TABLE =
            "CREATE TABLE " + MatchEntry.TABLE_NAME +
                    " (" +
                    MatchEntry._ID                              + " INTEGER PRIMARY KEY," +

                    MatchEntry.COLUMN_TEAM_NUMBER               + " INTEGER," +
                    MatchEntry.COLUMN_ALLY_1_TEAM_NUMBER        + " INTEGER," +
                    MatchEntry.COLUMN_ALLY_2_TEAM_NUMBER        + " INTEGER," +
                    MatchEntry.COLUMN_OPPONENT_1_TEAM_NUMBER    + " INTEGER," +
                    MatchEntry.COLUMN_OPPONENT_2_TEAM_NUMBER    + " INTEGER," +
                    MatchEntry.COLUMN_OPPONENT_3_TEAM_NUMBER    + " INTEGER," +

                    MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES    + " INTEGER," +
                    MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS     + " INTEGER," +
                    MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES   + " INTEGER," +
                    MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS    + " INTEGER," +

                    MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES      + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS       + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES   + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS    + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES   + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS    + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES   + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS    + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES   + " INTEGER," +
                    MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS    + " INTEGER" +
                    ")";

    private static final String SQL_DROP_MATCH_TABLE =
            "DROP TABLE IF EXISTS " + MatchEntry.TABLE_NAME;




    public ProfileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_PROFILE_TABLE);
        onCreate(db);
    }

}
