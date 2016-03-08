package com.example.brunoalmeida.frc2016scouting.database;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
public class DataExport {

    private static final String LOG_TAG = "DataExport";


    public static boolean exportDataToCSV(Context context) {

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
            ArrayList<Profile> profiles = ProfileDBHelper.readAllProfiles(context);

            writer.write("Team Number,Robot Function\n");
            for (Profile profile : profiles) {
                writer.write( String.format("%d,%s\n", profile.getTeamNumber(), profile.getRobotFunction()) );
            }
            writer.write("\n");




            // Write matches for each profile
            for (Profile profile : profiles) {
                ArrayList<Match> matches = ProfileDBHelper.readMatches(context, profile.getTeamNumber());

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
            context.sendBroadcast(intent);

        } catch (Exception e) {
            Log.v(LOG_TAG, "In exportDataToCSV(): operation failed" + "\n" + e.getLocalizedMessage());
        }

        return false;
    }

}
