package com.example.brunoalmeida.frc2016scouting;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void newProfile() {
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
