package com.example.brunoalmeida.frc2016scouting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.brunoalmeida.frc2016scouting.data.Match;
import com.example.brunoalmeida.frc2016scouting.data.Profile;
import com.example.brunoalmeida.frc2016scouting.database.ProfileDBHelper;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ExportDataActivity
        extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String LOG_TAG = "ExportDataActivity";

    private static int PERMISSION_REQUEST_CODE_EXPORT_DATA_TO_CSV = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    protected void onStart() {
        Log.v(LOG_TAG, "In onStart()");

        super.onStart();

        /* Check for write permission */

        // Permission is not already granded
        // Must request permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Request the write permission on Android 6.0+
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_CODE_EXPORT_DATA_TO_CSV);


/*            // Should we show an explanation?
            // "True if the app has requested this permission previously
            // and the user denied the request."
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                // PERMISSION_REQUEST_EXPORT_DATA_TO_CSV is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/


        // Permission is already granted
        } else {
            writeDataToMultipleCSVFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        Log.v(LOG_TAG, "In onRequestPermissionsResult()");

        if (requestCode == PERMISSION_REQUEST_CODE_EXPORT_DATA_TO_CSV) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // export-related task you need to do.
                writeDataToMultipleCSVFiles();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    private void writeDataToSingleCSVFile() {

        Log.v(LOG_TAG, "In writeDataToSingleCSVFile()");

        try {
            // Set up the file and writer
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downloadsDirectory.mkdir();
            Log.v(LOG_TAG, "downloadsDirectory: " + downloadsDirectory);

            File scoutingDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/FRCScouting");
            scoutingDirectory.mkdir();
            Log.v(LOG_TAG, "scoutingDirectory: " + scoutingDirectory);


            File file = new File(scoutingDirectory, "Scouting.csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.v(LOG_TAG, "file: " + file);

            FileWriter writer = new FileWriter(file);




            // Write profile data
            ArrayList<Profile> profiles = ProfileDBHelper.readAllProfiles(this);

            writer.write("Team Number,Robot Function\n");
            for (Profile profile : profiles) {
                writer.write( String.format("%d,%s\n", profile.getTeamNumber(), profile.getRobotFunction()) );
            }
            writer.write("\n");




            // Write match data (loop through each profile for its matches)
            for (Profile profile : profiles) {
                ArrayList<Match> matches = ProfileDBHelper.readMatches(this, profile.getTeamNumber());

                writer.write("\n");
                writer.write("Team " + profile.getTeamNumber() + "\n");

                String line = "";

                line += "Description,";

                for (Match.Team team : Match.Team.values()) {
                    line += team.getDisplayString() + ",";
                }
                for (Match.Shooting shooting : Match.Shooting.values()) {
                    line += shooting.getDisplayString() + ",";
                }
                for (Match.DefenseBreach defenseBreach : Match.DefenseBreach.values()) {
                    line += defenseBreach.getDisplayString() + ",";
                }

                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }

                writer.write(line + "\n");

                for (Match match : matches) {
                    line = "";

                    line += match.getDescription() + ",";

                    for (Match.Team team : Match.Team.values()) {
                        line += match.getTeamNumber(team) + ",";
                    }
                    for (Match.Shooting shooting : Match.Shooting.values()) {
                        line += match.getShootingRate(shooting) + ",";
                    }
                    for (Match.DefenseBreach defenseBreach : Match.DefenseBreach.values()) {
                        line += match.getDefenseBreachRate(defenseBreach) + ",";
                    }

                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    writer.write(line + "\n");
                }

                writer.write("\n");
            }




            // Cleanup
            writer.flush();
            writer.close();

            // Notify the system to make this file visible
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);

        } catch (Exception e) {
            Log.w(LOG_TAG, "In writeDataToSingleCSVFile(): operation failed" + "\n" + e.getLocalizedMessage());
        }
    }

    private void writeDataToMultipleCSVFiles() {

        Log.v(LOG_TAG, "In writeDataToMultipleCSVFiles()");

        try {
            // Set up the file and writer
            File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDirectory.exists()) {
                downloadsDirectory.mkdir();
                Log.v(LOG_TAG, "Created directory: " + downloadsDirectory);
            }
            Log.v(LOG_TAG, "downloadsDirectory: " + downloadsDirectory);

            File scoutingDirectory = new File(Environment.getExternalStorageDirectory().toString(), "FRCScouting");
            if (!scoutingDirectory.exists()) {
                scoutingDirectory.mkdir();
                Log.v(LOG_TAG, "Created directory: " + scoutingDirectory);
            }
            Log.v(LOG_TAG, "scoutingDirectory: " + scoutingDirectory);


            // Can modify to export to downloads directory instead
            File baseDirectory = scoutingDirectory;

            // Create a separate folder for each export operation
            File exportDirectory = null;
            int exportNumber = 1;

            while (true) {
                exportDirectory = new File(baseDirectory, "FRCScouting-" + exportNumber);

                if (exportDirectory.exists()) {
                    exportNumber++;
                } else {
                    break;
                }
            }

            exportDirectory.mkdir();


            writeProfileDataToCSVFile(exportDirectory);
            writeMatchDataToCSVFiles(exportDirectory);

        } catch (Exception e) {
            Log.w(LOG_TAG, "In writeDataToMultipleCSVFiles(): operation failed" + "\n" + e.getLocalizedMessage());
        }
    }

    private void writeProfileDataToCSVFile(File directory) {
        try {
            File file = new File(directory, "TeamList.csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.v(LOG_TAG, "Team list file: " + file);

            FileWriter writer = new FileWriter(file);

            // Write profile data
            ArrayList<Profile> profiles = ProfileDBHelper.readAllProfiles(this);

            writer.write("Team Number,Robot Function\n");
            for (Profile profile : profiles) {
                writer.write(String.format("%d,%s\n", profile.getTeamNumber(), profile.getRobotFunction()));
            }
            writer.write("\n");

            // Cleanup
            writer.flush();
            writer.close();

            // Notify the system to make this file visible
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);

        } catch (Exception e) {
            Log.w(LOG_TAG, e);
        }
    }

    private void writeMatchDataToCSVFiles(File directory) {
        /* Export profiles (match data) in separate files */

        try {

            // Write match data (loop through each profile for its matches)
            for (Profile profile : ProfileDBHelper.readAllProfiles(this)) {

                File file = new File(directory, "Team" + profile.getTeamNumber() + ".csv");
                if (!file.exists()) {
                    file.createNewFile();
                }
                Log.v(LOG_TAG, "Team " + profile.getTeamNumber() + " file: " + file);

                FileWriter writer = new FileWriter(file);

                ArrayList<Match> matches = ProfileDBHelper.readMatches(this, profile.getTeamNumber());


                // Write columns
                String line = "";

                line += "Description,";

                for (Match.Team team : Match.Team.values()) {
                    line += team.getDisplayString() + ",";
                }
                for (Match.Shooting shooting : Match.Shooting.values()) {
                    line += shooting.getDisplayString() + ",";
                }
                for (Match.DefenseBreach defenseBreach : Match.DefenseBreach.values()) {
                    line += defenseBreach.getDisplayString() + ",";
                }

                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }

                writer.write(line + "\n");


                // Write match data
                for (Match match : matches) {
                    line = "";

                    line += match.getDescription() + ",";

                    for (Match.Team team : Match.Team.values()) {
                        line += match.getTeamNumber(team) + ",";
                    }
                    for (Match.Shooting shooting : Match.Shooting.values()) {
                        line += match.getShootingRate(shooting) + ",";
                    }
                    for (Match.DefenseBreach defenseBreach : Match.DefenseBreach.values()) {
                        line += match.getDefenseBreachRate(defenseBreach) + ",";
                    }

                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }

                    writer.write(line + "\n");
                }

                writer.write("\n");

                // Cleanup
                writer.flush();
                writer.close();

                // Notify the system to make this file visible
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);
            }

        } catch (Exception e) {
            Log.w(LOG_TAG, e);
        }
    }

}
