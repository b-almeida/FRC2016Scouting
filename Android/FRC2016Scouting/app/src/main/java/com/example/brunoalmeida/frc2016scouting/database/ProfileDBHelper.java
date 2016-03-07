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
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Shooting;
import com.example.brunoalmeida.frc2016scouting.data.Match.DefenseBreach;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ColumnPair;

import com.example.brunoalmeida.frc2016scouting.data.Profile;

import java.util.ArrayList;
import java.util.EnumMap;

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
    private static final String SQL_CREATE_MATCH_TABLE;

    // Construct SQL_CREATE_MATCH_TABLE
    static {
        String str = "";

        str += "CREATE TABLE " + MatchEntry.TABLE_NAME + " (";

        str += MatchEntry._ID + " INTEGER PRIMARY KEY,";

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.TEAM_NUMBER_COLUMNS.entrySet()) {
            str += teamNumberColumn.getValue() + " INTEGER,";
        }

        for (EnumMap.Entry<Shooting, ColumnPair> shootingRateColumn : MatchEntry.SHOOTING_RATE_COLUMNS.entrySet()) {
            str += shootingRateColumn.getValue().getSuccesses() + " INTEGER,";
            str += shootingRateColumn.getValue().getAttempts() + " INTEGER,";
        }

        for (EnumMap.Entry<DefenseBreach, ColumnPair> defenseBreachRateColumn : MatchEntry.DEFENSE_BREACH_RATE_COLUMNS.entrySet()) {
            str += defenseBreachRateColumn.getValue().getSuccesses() + " INTEGER,";
            str += defenseBreachRateColumn.getValue().getAttempts() + " INTEGER,";
        }

        if (str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        str += ")";

        SQL_CREATE_MATCH_TABLE = str;
    }

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
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + ProfileContract.ProfileEntry.TABLE_NAME +
                " ORDER BY " + ProfileEntry.COLUMN_TEAM_NUMBER + " ASC", null);

        if (cursor.moveToFirst()) {
            while (! cursor.isAfterLast()) {
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(ProfileEntry._ID));

                Profile profile = readProfile(context, id);

                Log.v(LOG_TAG, "readAllProfiles():" + "\n" + profile);

                profiles.add(profile);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        profileDBHelper.close();

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
        String sortOrder = ProfileEntry.COLUMN_TEAM_NUMBER + " ASC";

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

        int columnIndex;

        columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_TEAM_NUMBER);
        int teamNumber = cursor.getInt(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_ROBOT_FUNCTION);
        String robotFunction = cursor.getString(columnIndex);

        Profile profile = new Profile(id, teamNumber, robotFunction);

        Log.v(LOG_TAG, "readProfile():" + "\n" + profile);

        cursor.close();
        db.close();
        profileDBHelper.close();

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

        Log.v(LOG_TAG, "writeProfile():" + "\n" + profile);
        Log.v(LOG_TAG, "writeProfile(): newRowID = " + newRowID);

        database.close();
        profileDBHelper.close();

        return newRowID;
    }


    // Match Read/Write Operations

    public static ArrayList<Match> readMatches(Context context, int teamNumber) {
        ArrayList<Match> matches = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MatchEntry._ID,
        };

        String selection = MatchEntry.TABLE_NAME + "." + MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.ALLY_1) + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(teamNumber)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "";
        for (Team team : Team.values()) {
            sortOrder += MatchEntry.TEAM_NUMBER_COLUMNS.get(team) + ", ";
        }
        sortOrder += MatchEntry._ID;

        Cursor cursor = db.query(
                MatchEntry.TABLE_NAME,      // The table to query
                projection,                 // The columns to return
                selection,                  // The columns for the WHERE clause
                selectionArgs,              // The values for the WHERE clause
                null,                       // don't group the rows
                null,                       // don't filter by row groups
                sortOrder                   // The sort order
        );

        if (cursor.moveToFirst()) {
            while (! cursor.isAfterLast()) {
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(MatchEntry._ID));

                Match match = readMatch(context, id);

                Log.v(LOG_TAG, "readMatches():" + "\n" + match);

                matches.add(match);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        profileDBHelper.close();

        return matches;
    }

    public static Match readMatch(Context context, long id) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        ArrayList<String> projectionArrayList = new ArrayList<>();

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.TEAM_NUMBER_COLUMNS.entrySet()) {
            projectionArrayList.add(teamNumberColumn.getValue());
        }

        for (EnumMap.Entry<Shooting, ColumnPair> shootingRateColumn : MatchEntry.SHOOTING_RATE_COLUMNS.entrySet()) {
            projectionArrayList.add(shootingRateColumn.getValue().getSuccesses());
            projectionArrayList.add(shootingRateColumn.getValue().getAttempts());
        }

        for (EnumMap.Entry<DefenseBreach, ColumnPair> defenseBreachRateColumn : MatchEntry.DEFENSE_BREACH_RATE_COLUMNS.entrySet()) {
            projectionArrayList.add(defenseBreachRateColumn.getValue().getSuccesses());
            projectionArrayList.add(defenseBreachRateColumn.getValue().getAttempts());
        }

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = projectionArrayList.toArray(new String[projectionArrayList.size()]);

        String selection = MatchEntry.TABLE_NAME + "." + MatchEntry._ID + " = ? ";

        String[] selectionArgs = new String[] {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = MatchEntry._ID + " DESC";

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

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.ALLY_1));
        int ally1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.ALLY_2));
        int ally2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.ALLY_3));
        int ally3TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.OPPONENT_1));
        int opponent1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.OPPONENT_2));
        int opponent2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.TEAM_NUMBER_COLUMNS.get(Team.OPPONENT_3));
        int opponent3TeamNumber = cursor.getInt(columnIndex);

        Match match = new Match(
                id,
                ally1TeamNumber,
                ally2TeamNumber,
                ally3TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);

        for (EnumMap.Entry<Shooting, ColumnPair> shootingRateColumn : MatchEntry.SHOOTING_RATE_COLUMNS.entrySet()) {
            columnIndex = cursor.getColumnIndexOrThrow(shootingRateColumn.getValue().getSuccesses());
            int successes = cursor.getInt(columnIndex);
            columnIndex = cursor.getColumnIndexOrThrow(shootingRateColumn.getValue().getAttempts());
            int attempts = cursor.getInt(columnIndex);

            match.setShootingRate(shootingRateColumn.getKey(), successes, attempts);
        }

        for (EnumMap.Entry<DefenseBreach, ColumnPair> defenseBreachRateColumn : MatchEntry.DEFENSE_BREACH_RATE_COLUMNS.entrySet()) {
            columnIndex = cursor.getColumnIndexOrThrow(defenseBreachRateColumn.getValue().getSuccesses());
            int successes = cursor.getInt(columnIndex);
            columnIndex = cursor.getColumnIndexOrThrow(defenseBreachRateColumn.getValue().getAttempts());
            int attempts = cursor.getInt(columnIndex);

            match.setDefenseBreachRate(defenseBreachRateColumn.getKey(), successes, attempts);
        }

        Log.v(LOG_TAG, "readMatch():" + "\n" + match);

        cursor.close();
        db.close();
        profileDBHelper.close();

        return match;
    }

    public static long writeMatch(Context context, Match match) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.TEAM_NUMBER_COLUMNS.entrySet()) {
            values.put(teamNumberColumn.getValue(), match.getTeamNumber(teamNumberColumn.getKey()));
        }

        for (EnumMap.Entry<Shooting, ColumnPair> shootingRateColumn : MatchEntry.SHOOTING_RATE_COLUMNS.entrySet()) {
            values.put(shootingRateColumn.getValue().getSuccesses(), match.getShootingRate(shootingRateColumn.getKey()).getSuccesses());
            values.put(shootingRateColumn.getValue().getAttempts(), match.getShootingRate(shootingRateColumn.getKey()).getAttempts());
        }

        for (EnumMap.Entry<DefenseBreach, ColumnPair> defenseBreachRateColumn : MatchEntry.DEFENSE_BREACH_RATE_COLUMNS.entrySet()) {
            values.put(defenseBreachRateColumn.getValue().getSuccesses(), match.getDefenseBreachRate(defenseBreachRateColumn.getKey()).getSuccesses());
            values.put(defenseBreachRateColumn.getValue().getAttempts(), match.getDefenseBreachRate(defenseBreachRateColumn.getKey()).getAttempts());
        }

        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileContract.MatchEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeMatch():" + "\n" + match);
        Log.v(LOG_TAG, "writeMatch(): newRowID = " + newRowID);

        database.close();
        profileDBHelper.close();

        return newRowID;
    }

    public static void updateMatch(Context context, Match match) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.TEAM_NUMBER_COLUMNS.entrySet()) {
            values.put(teamNumberColumn.getValue(), match.getTeamNumber(teamNumberColumn.getKey()));
        }

        for (EnumMap.Entry<Shooting, ColumnPair> shootingRateColumn : MatchEntry.SHOOTING_RATE_COLUMNS.entrySet()) {
            values.put(shootingRateColumn.getValue().getSuccesses(), match.getShootingRate(shootingRateColumn.getKey()).getSuccesses());
            values.put(shootingRateColumn.getValue().getAttempts(), match.getShootingRate(shootingRateColumn.getKey()).getAttempts());
        }

        for (EnumMap.Entry<DefenseBreach, ColumnPair> defenseBreachRateColumn : MatchEntry.DEFENSE_BREACH_RATE_COLUMNS.entrySet()) {
            values.put(defenseBreachRateColumn.getValue().getSuccesses(), match.getDefenseBreachRate(defenseBreachRateColumn.getKey()).getSuccesses());
            values.put(defenseBreachRateColumn.getValue().getAttempts(), match.getDefenseBreachRate(defenseBreachRateColumn.getKey()).getAttempts());
        }

        String strFilter = MatchEntry._ID + "=" + match.getID();

        // Insert the new row, returning the primary key value of the new row
        database.update(MatchEntry.TABLE_NAME, values, strFilter, null);

        Log.v(LOG_TAG, "updateMatch():" + "\n" + match);

        database.close();
        profileDBHelper.close();
    }

}
