package com.example.brunoalmeida.frc2016scouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.SuccessRate;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;

import com.example.brunoalmeida.frc2016scouting.data.Profile;

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
                    ProfileEntry.COLUMN_ROBOT_FUNCTION  + " TEXT" +
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




    // Profile Read/Write Operations

    public static ArrayList<Profile> readAllProfiles(Context context) {
        ArrayList<Profile> profiles = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
        SQLiteDatabase db = profileDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ProfileContract.ProfileEntry.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            while (! cursor.isAfterLast()) {
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(ProfileEntry._ID));
                int teamNumber = cursor.getInt(
                        cursor.getColumnIndex(ProfileEntry.COLUMN_TEAM_NUMBER));
                String robotFunction = cursor.getString(
                        cursor.getColumnIndex(ProfileEntry.COLUMN_ROBOT_FUNCTION));

                Profile profile = new Profile(id, teamNumber, robotFunction);

                Log.v(LOG_TAG, "readProfilesFromDB():" + "\n" + profile);

                profiles.add(profile);
                cursor.moveToNext();
            }
        }

        return profiles;
    }

    public static Profile readProfile(Context context, long id) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProfileEntry.COLUMN_TEAM_NUMBER,
                ProfileEntry.COLUMN_ROBOT_FUNCTION
        };

        String selection = ProfileEntry.TABLE_NAME + "." + ProfileEntry._ID + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ProfileEntry.COLUMN_TEAM_NUMBER + " DESC";

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

        int teamNumberColumnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_TEAM_NUMBER);
        int teamNumber = cursor.getInt(teamNumberColumnIndex);

        int robotFunctionColumnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_ROBOT_FUNCTION);
        String robotFunction = cursor.getString(robotFunctionColumnIndex);

        Profile profile = new Profile(id, teamNumber, robotFunction);

        Log.v(LOG_TAG, "readProfileFromDB():" + "\n" + profile);

        return profile;
    }

    public static long writeProfile(Context context, Profile profile) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProfileEntry.COLUMN_TEAM_NUMBER, profile.getTeamNumber());
        values.put(ProfileEntry.COLUMN_ROBOT_FUNCTION, profile.getRobotFunction());

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeProfileToDB(): newRowID = " + newRowID);

        return newRowID;
    }



    // Match Read/Write Operations

    public static Match readMatch(Context context, long id) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MatchEntry.COLUMN_TEAM_NUMBER,
                MatchEntry.COLUMN_ALLY_1_TEAM_NUMBER,
                MatchEntry.COLUMN_ALLY_2_TEAM_NUMBER,
                MatchEntry.COLUMN_OPPONENT_1_TEAM_NUMBER,
                MatchEntry.COLUMN_OPPONENT_2_TEAM_NUMBER,
                MatchEntry.COLUMN_OPPONENT_3_TEAM_NUMBER,

                MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES,
                MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS,
                MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES,
                MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS,

                MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES,
                MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES,
                MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS
        };

        String selection = MatchEntry.TABLE_NAME + "." + MatchEntry._ID + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = MatchEntry.COLUMN_TEAM_NUMBER + " DESC";

        Cursor cursor = db.query(
                MatchEntry.TABLE_NAME,      // The table to query
                projection,                 // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,              // The values for the WHERE clause
                null,                       // don't group the rows
                null,                       // don't filter by row groups
                sortOrder                   // The sort order
        );

        cursor.moveToFirst();

        int columnIndex;

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_TEAM_NUMBER);
        int teamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_ALLY_1_TEAM_NUMBER);
        int ally1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_ALLY_2_TEAM_NUMBER);
        int ally2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_OPPONENT_1_TEAM_NUMBER);
        int opponent1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_OPPONENT_2_TEAM_NUMBER);
        int opponent2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_OPPONENT_3_TEAM_NUMBER);
        int opponent3TeamNumber = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES);
        int lowShootingSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS);
        int lowShootingAttempts = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES);
        int highShootingSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS);
        int highShootingAttempts = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES);
        int lowBarBreachSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS);
        int lowBarBreachAttempts = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES);
        int categoryABreachSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS);
        int categoryABreachAttempts = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES);
        int categoryBBreachSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS);
        int categoryBBreachAttempts = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES);
        int categoryCBreachSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS);
        int categoryCBreachAttempts = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES);
        int categoryDBreachSuccesses = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS);
        int categoryDBreachAttempts = cursor.getInt(columnIndex);

        Match match  = new Match(
                id,
                teamNumber,
                ally1TeamNumber,
                ally2TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber,

                new SuccessRate(lowShootingSuccesses, lowShootingAttempts),
                new SuccessRate(highShootingSuccesses, highShootingAttempts),

                new SuccessRate(lowBarBreachSuccesses, lowBarBreachAttempts),
                new SuccessRate(categoryABreachSuccesses, categoryABreachAttempts),
                new SuccessRate(categoryBBreachSuccesses, categoryBBreachAttempts),
                new SuccessRate(categoryCBreachSuccesses, categoryCBreachAttempts),
                new SuccessRate(categoryDBreachSuccesses, categoryDBreachAttempts));

        Log.v(LOG_TAG, "readMatch():" + "\n" + match);

        return match;
    }

    public static long writeMatch(Context context, Match match) {
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

        values.put(MatchEntry.COLUMN_LOW_SHOOTING_SUCCESSES, match.getLowShootingSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_LOW_SHOOTING_ATTEMPTS, match.getLowShootingSuccessRate().getAttempts());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_SUCCESSES, match.getHighShootingSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_HIGH_SHOOTING_ATTEMPTS, match.getHighShootingSuccessRate().getAttempts());

        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_SUCCESSES, match.getDefenseLowBarBreachSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_LOW_BAR_BREACH_ATTEMPTS, match.getDefenseLowBarBreachSuccessRate().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_SUCCESSES, match.getDefenseCategoryABreachSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_A_BREACH_ATTEMPTS, match.getDefenseCategoryABreachSuccessRate().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_SUCCESSES, match.getDefenseCategoryBBreachSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_B_BREACH_ATTEMPTS, match.getDefenseCategoryBBreachSuccessRate().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_SUCCESSES, match.getDefenseCategoryCBreachSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_C_BREACH_ATTEMPTS, match.getDefenseCategoryCBreachSuccessRate().getAttempts());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_SUCCESSES, match.getDefenseCategoryDBreachSuccessRate().getSuccesses());
        values.put(MatchEntry.COLUMN_DEFENSE_CATEGORY_D_BREACH_ATTEMPTS, match.getDefenseCategoryDBreachSuccessRate().getAttempts());

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileContract.MatchEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeMatchToDB(): newRowID = " + newRowID);

        return newRowID;
    }

}
