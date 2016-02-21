package com.example.brunoalmeida.frc2016scouting;

//import android.app.ActionBar;             // modern action bar
import android.app.Activity;
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
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button newProfile = (Button) view.findViewById(R.id.new_profile);
        newProfile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_profile:
                newProfile();
                break;
            default:
                Log.w(LOG_TAG, "onClick not handled");
        }
    }

    private void newProfile() {
        Log.v(LOG_TAG, "in newProfile()");

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
