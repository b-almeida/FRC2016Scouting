package com.example.brunoalmeida.frc2016scouting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Statistic;
import com.example.brunoalmeida.frc2016scouting.data.SuccessRate;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ProfileEntry;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.MatchEntry;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.database.ProfileContract.ColumnPair;

import com.example.brunoalmeida.frc2016scouting.data.Profile;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Created by Bruno on 2016-02-22.
 */
public class ProfileDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Profiles.db";
    private static final String LOG_TAG = "ProfileDBHelper";
    private static final int DATABASE_VERSION = 1;


    // Profile Table - SQL operations
    private static final String SQL_CREATE_PROFILE_TABLE =
            "CREATE TABLE " + ProfileEntry.TABLE_NAME +
                    " (" +
                    ProfileEntry._ID + " INTEGER PRIMARY KEY," +
                    ProfileEntry.COLUMN_TEAM_NUMBER + " INTEGER," +
                    ProfileEntry.COLUMN_DESCRIPTION + " TEXT," +
                    ProfileEntry.COLUMN_ROBOT_FUNCTION + " TEXT," +
                    ProfileEntry.COLUMN_NOTES + " TEXT" +
                    ")";

    private static final String SQL_DROP_PROFILE_TABLE =
            "DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME;


    // Match Table - SQL operations
    private static final String SQL_CREATE_MATCH_TABLE;
    private static final String SQL_DROP_MATCH_TABLE =
            "DROP TABLE IF EXISTS " + MatchEntry.TABLE_NAME;

    // Construct SQL_CREATE_MATCH_TABLE
    static {
        String str = "";

        str += "CREATE TABLE " + MatchEntry.TABLE_NAME + " (";


        str += MatchEntry._ID + " INTEGER PRIMARY KEY,";

        str += MatchEntry.COLUMN_DESCRIPTION + " TEXT,";

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.COLUMNS_TEAM_NUMBERS.entrySet()) {
            str += teamNumberColumn.getValue() + " INTEGER,";
        }

        for (EnumMap.Entry<Statistic, ColumnPair> statisticColumn : MatchEntry.COLUMNS_STATISTICS.entrySet()) {
            str += statisticColumn.getValue().getSuccesses() + " INTEGER,";
            str += statisticColumn.getValue().getAttempts() + " INTEGER,";
        }


        if (str.length() > 0 && str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        str += ")";

        SQL_CREATE_MATCH_TABLE = str;
    }


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



    public static ArrayList<Profile> readAllProfiles(Context context) {
        ArrayList<Profile> profiles = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
        SQLiteDatabase database = profileDBHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + ProfileContract.ProfileEntry.TABLE_NAME +
                        " ORDER BY " + ProfileEntry.COLUMN_TEAM_NUMBER, null);

        if (cursor.moveToFirst()) {
            while (! cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ProfileEntry._ID));

                Profile profile = readProfile(context, id);

                Log.v(LOG_TAG, "readAllProfiles(): " + profile);

                profiles.add(profile);
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        profileDBHelper.close();

        return profiles;
    }

    public static Profile readProfile(Context context, long id) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
        SQLiteDatabase database = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProfileEntry.COLUMN_TEAM_NUMBER,
                ProfileEntry.COLUMN_DESCRIPTION,
                ProfileEntry.COLUMN_ROBOT_FUNCTION,
                ProfileEntry.COLUMN_NOTES
        };

        String selection = ProfileEntry.TABLE_NAME + "." + ProfileEntry._ID + " = ? ";

        String[] selectionArgs = new String[] { String.valueOf(id) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ProfileEntry.COLUMN_TEAM_NUMBER;

        Cursor cursor = database.query(
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

        columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_DESCRIPTION);
        String description = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_ROBOT_FUNCTION);
        String robotFunction = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_NOTES);
        String notes = cursor.getString(columnIndex);


        Profile profile = new Profile(id, teamNumber, description, robotFunction, notes);

        Log.v(LOG_TAG, "readProfile(): " + profile);

        cursor.close();
        database.close();
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
        values.put(ProfileEntry.COLUMN_DESCRIPTION, profile.getDescription());
        values.put(ProfileEntry.COLUMN_ROBOT_FUNCTION, profile.getRobotFunction());
        values.put(ProfileEntry.COLUMN_NOTES, profile.getNotes());


        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeProfile(): " + profile);
        Log.v(LOG_TAG, "writeProfile(): newRowID = " + newRowID);

        database.close();
        profileDBHelper.close();

        return newRowID;
    }


    public static ArrayList<Match> readMatches(Context context, int teamNumber) {
        ArrayList<Match> matches = new ArrayList<>();

        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
        SQLiteDatabase db = profileDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                MatchEntry._ID,
        };

        String selection = MatchEntry.TABLE_NAME + "." + MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.ALLY_1) + " = ? ";

        String[] selectionArgs = new String[] { String.valueOf(teamNumber) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = MatchEntry._ID;

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
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MatchEntry._ID));

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

        SQLiteDatabase database = profileDBHelper.getReadableDatabase();


        ArrayList<String> projectionArrayList = new ArrayList<>();

        projectionArrayList.add(MatchEntry.COLUMN_DESCRIPTION);

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.COLUMNS_TEAM_NUMBERS.entrySet()) {
            projectionArrayList.add(teamNumberColumn.getValue());
        }

        for (EnumMap.Entry<Statistic, ColumnPair> statisticColumn : MatchEntry.COLUMNS_STATISTICS.entrySet()) {
            projectionArrayList.add(statisticColumn.getValue().getSuccesses());
            projectionArrayList.add(statisticColumn.getValue().getAttempts());
        }


        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = projectionArrayList.toArray(new String[projectionArrayList.size()]);

        String selection = MatchEntry.TABLE_NAME + "." + MatchEntry._ID + " = ? ";

        String[] selectionArgs = new String[] { String.valueOf(id) };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = MatchEntry._ID;


        Cursor cursor = database.query(
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

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMN_DESCRIPTION);
        String description = cursor.getString(columnIndex);

        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.ALLY_1));
        int ally1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.ALLY_2));
        int ally2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.ALLY_3));
        int ally3TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.OPPONENT_1));
        int opponent1TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.OPPONENT_2));
        int opponent2TeamNumber = cursor.getInt(columnIndex);
        columnIndex = cursor.getColumnIndexOrThrow(MatchEntry.COLUMNS_TEAM_NUMBERS.get(Team.OPPONENT_3));
        int opponent3TeamNumber = cursor.getInt(columnIndex);

        Match match = new Match(
                id,
                description,
                ally1TeamNumber,
                ally2TeamNumber,
                ally3TeamNumber,
                opponent1TeamNumber,
                opponent2TeamNumber,
                opponent3TeamNumber);

        for (EnumMap.Entry<Statistic, ColumnPair> statisticColumn : MatchEntry.COLUMNS_STATISTICS.entrySet()) {
            columnIndex = cursor.getColumnIndexOrThrow(statisticColumn.getValue().getSuccesses());
            int successes = cursor.getInt(columnIndex);

            columnIndex = cursor.getColumnIndexOrThrow(statisticColumn.getValue().getAttempts());
            int attempts = cursor.getInt(columnIndex);

            match.setStatistic(statisticColumn.getKey(), successes, attempts);
        }


        Log.v(LOG_TAG, "readMatch():" + "\n" + match);

        cursor.close();
        database.close();
        profileDBHelper.close();

        return match;
    }

    public static long writeMatch(Context context, Match match) {
        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase database = profileDBHelper.getWritableDatabase();


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(MatchEntry.COLUMN_DESCRIPTION, match.getDescription());

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.COLUMNS_TEAM_NUMBERS.entrySet()) {
            values.put(teamNumberColumn.getValue(), match.getTeamNumber(teamNumberColumn.getKey()));
        }

        for (EnumMap.Entry<Statistic, ColumnPair> statisticColumn : MatchEntry.COLUMNS_STATISTICS.entrySet()) {
            values.put(statisticColumn.getValue().getSuccesses(), match.getStatistic(statisticColumn.getKey()).getSuccesses());
            values.put(statisticColumn.getValue().getAttempts(), match.getStatistic(statisticColumn.getKey()).getAttempts());
        }


        // Insert the new row, returning the primary key value of the new row
        long newRowID = database.insert(
                ProfileContract.MatchEntry.TABLE_NAME,
                null,
                values);

        Log.v(LOG_TAG, "writeMatch(): " + match);
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

        values.put(MatchEntry.COLUMN_DESCRIPTION, match.getDescription());

        for (EnumMap.Entry<Team, String> teamNumberColumn : MatchEntry.COLUMNS_TEAM_NUMBERS.entrySet()) {
            values.put(teamNumberColumn.getValue(), match.getTeamNumber(teamNumberColumn.getKey()));
        }

        for (EnumMap.Entry<Statistic, ColumnPair> statisticColumn : MatchEntry.COLUMNS_STATISTICS.entrySet()) {
            values.put(statisticColumn.getValue().getSuccesses(), match.getStatistic(statisticColumn.getKey()).getSuccesses());
            values.put(statisticColumn.getValue().getAttempts(), match.getStatistic(statisticColumn.getKey()).getAttempts());
        }


        String strFilter = MatchEntry._ID + " = " + match.getID();

        // Insert the new row, returning the primary key value of the new row
        database.update(
                MatchEntry.TABLE_NAME,
                values,
                strFilter,
                null);

        Log.v(LOG_TAG, "updateMatch(): " + match);

        database.close();
        profileDBHelper.close();
    }

}
