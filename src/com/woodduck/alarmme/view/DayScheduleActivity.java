package com.woodduck.alarmme.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.woodduck.alarmme.R;

public class DayScheduleActivity extends Activity {
    private String TAG = "DayScheduleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        LinearLayout layout = (LinearLayout) findViewById(R.id.dayview_layout);
        if(layout != null){
            Log.d(TAG, "Day view layout...");
        }

    }
}
