package com.example.brunoalmeida.frc2016scouting;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewMatchActivityFragment extends Fragment {

    private static final String LOG_TAG = "NewMatchAFragment";


    public NewMatchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_new_match, container, false);

        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout);

        // Hide the keyboard when the layout is touched
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(LOG_TAG, "layout onTouch");

                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);

                return true;
            }
        });

        return rootView;
    }

}
