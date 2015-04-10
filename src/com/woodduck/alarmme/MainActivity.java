
package com.woodduck.alarmme;

import java.util.Calendar;

import com.woodduck.alarmme.adapter.AlarmListAdapter;
import com.woodduck.alarmme.database.ItemDAO;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity {
    private String TAG = "AlarmMeMain";
    private ImageButton mAddTask;
    private Button mAlarm;
    AlarmListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
    }

    private void initListView() {
        mAdapter = new AlarmListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);

        mAlarm = (Button) findViewById(R.id.alarm_me);
        mAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSetup();
                showDatePicker();
            }
        });

        mAddTask = (ImageButton) findViewById(R.id.add_item);
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddTast();
            }
        });
    }

    // alarm
    public void onClickSetup() {
        Log.d(TAG, "do onClickSetup..ddd.");
        Calendar cal = Calendar.getInstance();
        // 設定於 3 分鐘後執行
        // cal.add(Calendar.MINUTE, 1);
        // please not.. the month start from 0--> Jan 1-> Feb
        cal.set(2015, Calendar.APRIL, 02, 18, 22);

        Intent intent = new Intent(this, PlayReceiver.class);
        intent.putExtra("msg", "play_hskay");

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    // Note: Date picker has different display type..
    private DatePickerDialog showDatePicker() {
        Calendar dueDateCalendar = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this, null, dueDateCalendar.get(Calendar.YEAR),
                dueDateCalendar.get(Calendar.MONTH), dueDateCalendar.get(Calendar.DAY_OF_MONTH)) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                int year = getContext().getResources().getIdentifier("android:id/year", null, null);
                if (year != 0) {
                    View yearPicker = findViewById(year);
                    if (yearPicker != null) {
                        yearPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        dlg.show();
        return dlg;
    }

    private int ADD_TASK = 100;

    private void startAddTast() {
        Intent addTask = new Intent(this, AddTaskActivity.class);
        this.startActivityForResult(addTask, ADD_TASK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "RESULT OK " + data);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "RESULT OK Cancel");
            } else {
                Log.d(TAG, "RESULT What..");
            }
        }
    }
}
