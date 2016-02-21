package com.example.brunoalmeida.frc2016scouting;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
        }
    }

    private void newProfile() {
        Log.v(LOG_TAG, "in newProfile()");
    }

}
