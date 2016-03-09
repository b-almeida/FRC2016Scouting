package com.example.brunoalmeida.frc2016scouting.database;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Match.Team;
import com.example.brunoalmeida.frc2016scouting.data.Match.Shooting;
import com.example.brunoalmeida.frc2016scouting.data.Match.DefenseBreach;
import com.example.brunoalmeida.frc2016scouting.data.Profile;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Exports data from the database to files in public directories on the device.
 * Created by Bruno on 2016-03-05.
 */
public class DataExport implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String LOG_TAG = "DataExport";

    private static int PERMISSION_REQUEST_EXPORT_DATA_TO_CSV = 1;




    public static void exportDataToCSV(Activity activity) {
        Log.v(LOG_TAG, "In exportDataToCSV()");

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            // "True if the app has requested this permission previously
            // and the user denied the request."
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        PERMISSION_REQUEST_EXPORT_DATA_TO_CSV);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        PERMISSION_REQUEST_EXPORT_DATA_TO_CSV);

                // PERMISSION_REQUEST_EXPORT_DATA_TO_CSV is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        Log.v(LOG_TAG, "In onRequestPermissionsResult()");

        if (requestCode == PERMISSION_REQUEST_EXPORT_DATA_TO_CSV) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // export-related task you need to do.
                writeDataToCSV(new Activity());

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    private static boolean writeDataToCSV(Activity activity) {

        Log.v(LOG_TAG, "In writeDataToCSV");

        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File file = new File(directory, "Scouting.csv");

            Log.v(LOG_TAG, "directory: " + directory);
            Log.v(LOG_TAG, "file: " + file);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);




            //writer.write("this is a test");
/*            String line = String.format("%s,%s,%s\n", h1,h2,h3);
            writer.write(line);*/

            // Write profiles
            ArrayList<Profile> profiles = ProfileDBHelper.readAllProfiles(activity);

            writer.write("Team Number,Robot Function\n");
            for (Profile profile : profiles) {
                writer.write( String.format("%d,%s\n", profile.getTeamNumber(), profile.getRobotFunction()) );
            }
            writer.write("\n");




            // Write matches for each profile
            for (Profile profile : profiles) {
                ArrayList<Match> matches = ProfileDBHelper.readMatches(activity, profile.getTeamNumber());

                writer.write("\n");
                writer.write("Team " + profile.getTeamNumber() + "\n");

                String line = "";

                line += "Description,";

                for (Team team : Team.values()) {
                    line += team.getDisplayString() + ",";
                }
                for (Shooting shooting : Shooting.values()) {
                    line += shooting.getDisplayString() + ",";
                }
                for (DefenseBreach defenseBreach : DefenseBreach.values()) {
                    line += defenseBreach.getDisplayString() + ",";
                }

                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }

                writer.write(line + "\n");

                for (Match match : matches) {
                    line = "";

                    line += match.getDescription() + ",";

                    for (Team team : Team.values()) {
                        line += match.getTeamNumber(team) + ",";
                    }
                    for (Shooting shooting : Shooting.values()) {
                        line += match.getShootingRate(shooting) + ",";
                    }
                    for (DefenseBreach defenseBreach : DefenseBreach.values()) {
                        line += match.getDefenseBreachRate(defenseBreach) + ",";
                    }

                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    writer.write(line + "\n");
                }

                writer.write("\n");
            }




            writer.flush();
            writer.close();

            // Notify the system to make this file visible
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            activity.sendBroadcast(intent);

        } catch (Exception e) {
            Log.v(LOG_TAG, "In writeDataToCSV(): operation failed" + "\n" + e.getLocalizedMessage());
        }

        return false;
    }

}
