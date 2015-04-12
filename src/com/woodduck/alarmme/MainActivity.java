
package com.woodduck.alarmme;

import java.util.List;

import com.woodduck.alarmme.adapter.AlarmListAdapter;
import com.woodduck.alarmme.database.ItemDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        getList();
        mAddTask = (ImageButton) findViewById(R.id.add_item);
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddTast();
            }
        });
        listView.setLongClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Log.d(TAG, "show click ");
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "long click ");
                showDialog();
                return true;
            }
        });
    }

    private void getList() {
        ItemDAO dao = new ItemDAO(this);
        List<EventItem> list = dao.getAll();
        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
    }

    private void startAddTast() {
        Intent addTask = new Intent(this, AddTaskActivity.class);
        this.startActivityForResult(addTask, ADD_TASK);
    }

    private void showDialog() {
        Log.d(TAG, "show dialog ");
        new AlertDialog.Builder(this)
                .setTitle(R.string.opt_title)
                .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "RESULT OK " + data);
                getList();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "RESULT OK Cancel");
            } else {
                Log.d(TAG, "RESULT What..");
            }
        }
    }
}
