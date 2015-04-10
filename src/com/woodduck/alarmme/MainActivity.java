
package com.woodduck.alarmme;

import com.woodduck.alarmme.adapter.AlarmListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity {
    private String TAG = "AlarmMeMain";
    private ImageButton mAddTask;
    private AlarmListAdapter mAdapter;
    private int ADD_TASK = 100;

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

        mAddTask = (ImageButton) findViewById(R.id.add_item);
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddTast();
            }
        });
    }

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
