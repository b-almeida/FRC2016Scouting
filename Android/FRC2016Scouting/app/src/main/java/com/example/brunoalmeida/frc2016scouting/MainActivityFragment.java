package com.example.brunoalmeida.frc2016scouting;

//import android.app.ActionBar;             // modern action bar
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.app.ActionBar;    // support action bar
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = "MainActivityFragment";


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button newProfile = (Button) rootView.findViewById(R.id.new_profile);
        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newProfile();
            }
        });

        return rootView;
    }

    private void newProfile() {
        Log.v(LOG_TAG, "In newProfile(): new_profile button tapped");

        // Switch to NewProfileActivity
        Intent intent = new Intent(getActivity(), NewProfileActivity.class);
        startActivity(intent);

        Log.v(LOG_TAG, "Switching to NewProfileActivity");

        // Test - Replace one fragment with another
        /*getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new NewProfileFragment())
                .addToBackStack(null)
                .commit();*/


        // Test - Set the ActionBar's title from a Fragment
/*        AppCompatActivity a = (AppCompatActivity) getActivity();

        if (a != null) {
            ActionBar ab = a.getSupportActionBar();

            if (ab != null) {
                ab.setTitle("Test");
            } else {
                Log.w(LOG_TAG, "action bar is null");
            }
        } else {
            Log.w(LOG_TAG, "activity is null");
        }*/
    }

}
