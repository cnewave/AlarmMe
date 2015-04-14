
package com.woodduck.alarmme;

import java.util.List;

import com.woodduck.alarmme.adapter.AlarmListAdapter;
import com.woodduck.alarmme.database.ItemDAO;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
    private String TAG = "AlarmMeMain";
    private ImageButton mAddTask;
    private AlarmListAdapter mAdapter;
    List<EventItem> mList;
    private int ADD_TASK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
        createNavigation();
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
                startAddTask();
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
                int _id = mList.get(position).getId();
                Log.d(TAG, "long click " + position + " id:" + _id);

                showOptionDialog(_id);
                return true;
            }
        });
    }

    private void getList() {
        ItemDAO dao = new ItemDAO(this);
        mList = dao.getAll();
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
    }

    private void createNavigation() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab tab = actionBar.newTab()
                .setText("Today").setTabListener(new TabListener());

        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText("Weekly")
                .setTabListener(new TabListener());
        actionBar.addTab(tab);
        tab = actionBar.newTab()
                .setText("All")
                .setTabListener(new TabListener());
        actionBar.addTab(tab);
    }

    public static class TabListener implements ActionBar.TabListener {

        @Override
        public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0, FragmentTransaction arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0, FragmentTransaction arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0, FragmentTransaction arg1) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAddTask() {
        Intent addTask = new Intent(this, AddTaskActivity.class);
        this.startActivityForResult(addTask, ADD_TASK);
    }

    private void startEditTask(final int _id) {
        Intent addTask = new Intent(this, AddTaskActivity.class);
        addTask.putExtra("_id", _id);
        this.startActivityForResult(addTask, ADD_TASK);
    }

    private void showOptionDialog(final int _id) {
        Log.d(TAG, "show dialog " + _id);
        new AlertDialog.Builder(this)
                .setTitle(R.string.opt_title)
                .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "show dialog " + which);
                        if (which == 0) {
                            startEditTask(_id);
                        }
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
