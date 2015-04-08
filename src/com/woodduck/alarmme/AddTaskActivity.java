package com.woodduck.alarmme;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog.OnDateSetListener;

public class AddTaskActivity extends Activity {
    private String TAG = "AlarmMeMain";
    private Button mDateButton;
    private Button mTimeButton;
    private Button mOKButton;
    private Button mCancelButton;

    Calendar rightNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addalarmtask);
        initUI();
    }

    private void initFragement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AudioFragment newFragment = AudioFragment.newInstance();
        ft.add(R.id.recorder_page, newFragment);
        ft.commit();
    }

    private void initUI() {
        rightNow = Calendar.getInstance();
        mDateButton = (Button) findViewById(R.id.datechooser);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch date picker.
                launchDatePicker();
            }
        });
        mDateButton.setText(getToday());

        mTimeButton = (Button) findViewById(R.id.timechooser);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch time picker
                launchTimePicker();
            }
        });
        mTimeButton.setText(getCurrentTime());

        mOKButton = (Button) findViewById(R.id.ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                saveTask();
            }
        });
        mCancelButton = (Button) findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask();
            }
        });
        initFragement();
    }

    private void saveTask() {
        //
        // Create a thread to save to DB.

        setResult(RESULT_OK);
        finish();
    }

    private void cancelTask() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private String getToday() {
        StringBuilder today = new StringBuilder(rightNow.get(Calendar.MONTH) + 1 + "/" + rightNow.get(Calendar.DATE));
        Log.d(TAG, "time :" + rightNow.getTime());
        Log.d(TAG, "Year :" + rightNow.get(Calendar.YEAR));
        Log.d(TAG, "Month :" + rightNow.get(Calendar.MONTH));
        Log.d(TAG, "Date :" + rightNow.get(Calendar.DATE));
        return today.toString();
    }

    private String getCurrentTime() {
        StringBuilder currentTime = new StringBuilder(rightNow.get(Calendar.HOUR_OF_DAY) + ":"
                + rightNow.get(Calendar.MINUTE));

        Log.d(TAG, "hour :" + rightNow.get(Calendar.HOUR_OF_DAY));
        Log.d(TAG, "minutes :" + rightNow.get(Calendar.MINUTE));
        return currentTime.toString();
    }

    private void launchDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, 0, mDateListenee, rightNow.get(Calendar.YEAR),
                rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DATE));
        dialog.show();
    }

    private void launchTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(this, 0, mTimePickeer, rightNow.get(Calendar.HOUR_OF_DAY),
                rightNow.get(Calendar.MINUTE), false);
        dialog.show();
    }

    DatePickListener mDateListenee = new DatePickListener();

    class DatePickListener implements OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d(TAG, "time :" + dayOfMonth);
            mDateButton.setText((monthOfYear + 1) + "/" + dayOfMonth);
            rightNow.set(Calendar.MONTH, monthOfYear);
            rightNow.set(Calendar.DATE, dayOfMonth);
        }
    }

    TimePickListener mTimePickeer = new TimePickListener();

    class TimePickListener implements OnTimeSetListener {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTimeButton.setText((hourOfDay) + ":" + minute);
            rightNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
            rightNow.set(Calendar.MINUTE, minute);
        }
    }
}
