package com.example.brunoalmeida.frc2016scouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.brunoalmeida.frc2016scouting.Match;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;

import com.example.brunoalmeida.frc2016scouting.Profile;

import java.util.ArrayList;

/**
 * Created by Bruno on 2016-02-22.
 */
public class ProfileDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "ProfileDBHelper";

    public static final String DATABASE_NAME = "Profiles.db";
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
        db.execSQL(SQL_CREATE_MATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_PROFILE_TABLE);
        db.execSQL(SQL_DROP_MATCH_TABLE);
        onCreate(db);
    }




    // Read/Write Operations

    public static ArrayList<Profile> readAllProfilesFromDB(Context context) {
        ArrayList<Profile> profiles = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
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

    public static String readRobotTypeFromDB(Context context, int teamNumber) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProfileEntry.COLUMN_ROBOT_TYPE
        };

        String selection = ProfileEntry.TABLE_NAME +
                "." + ProfileEntry.COLUMN_TEAM_NUMBER + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(teamNumber)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProfileEntry.COLUMN_ROBOT_TYPE + " DESC";

        Cursor cursor = db.query(
                ProfileEntry.TABLE_NAME,    // The table to query
                projection,                 // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,              // The values for the WHERE clause
                null,                       // don't group the rows
                null,                       // don't filter by row groups
                sortOrder                   // The sort order
        );

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_ROBOT_TYPE);
        String robotType = cursor.getString(columnIndex);

        Log.v(LOG_TAG, "readRobotTypeFromDB(): robotType = " + robotType);

        return robotType;
    }

    public static void writeProfileToDB(Context context, int teamNumber, String robotType) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProfileEntry.COLUMN_TEAM_NUMBER, teamNumber);
        values.put(ProfileEntry.COLUMN_ROBOT_TYPE, robotType);

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeProfileToDB(): newRowID = " + newRowID);
    }

    public static void writeMatchToDB(Context context, Match match) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

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

        values.put(MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES, match.getLowShootingSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS, match.getLowShootingSuccess().getAttempts());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES, match.getHighShootingSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS, match.getHighShootingSuccess().getAttempts());

        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES, match.getDefenseLowBarBreachSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS, match.getDefenseLowBarBreachSuccess().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES, match.getDefenseCategoryABreachSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS, match.getDefenseCategoryABreachSuccess().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES, match.getDefenseCategoryBBreachSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS, match.getDefenseCategoryBBreachSuccess().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES, match.getDefenseCategoryCBreachSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS, match.getDefenseCategoryCBreachSuccess().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES, match.getDefenseCategoryDBreachSuccess().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS, match.getDefenseCategoryDBreachSuccess().getAttempts());

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileContract.MatchEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeMatchToDB(): newRowID = " + newRowID);
    }

}
