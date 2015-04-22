
package com.woodduck.alarmme.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.woodduck.alarmme.AddEventActivity;
import com.woodduck.alarmme.EventItem;
import com.woodduck.alarmme.R;
import com.woodduck.alarmme.database.ItemDAO;

public class DayScheduleActivity extends Activity {
    private String TAG = "DaySchedule_TAG";
    List<EventItem> mList;
    /** layout for day, week view */
    LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        mLayout = (LinearLayout) findViewById(R.id.dayview_layout);
        if (mLayout != null) {
            Log.d(TAG, "Day view layout...");
        }
        mHandler.sendEmptyMessage(INIT_DATA);
        mHandler.sendEmptyMessage(SHOW_UI);
    }

    private void initAllList() {
        ItemDAO dao = new ItemDAO(this);
        mList = dao.getAll();
    }

    private final int INIT_DATA = 1;
    private final int SHOW_UI = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_DATA:
                    initAllList();
                    break;
                case SHOW_UI:
                    drawRectangle();
                    break;
            }
        }
    };
    private DailyView mDayView;

    private void drawRectangle() {
        Log.d(TAG, "Day drawRectangle getWidth:" + (int) getWindowManager()
                .getDefaultDisplay().getWidth());
        Log.d(TAG, "Day drawRectangle getHeight: " + (int) getWindowManager()
                .getDefaultDisplay().getHeight());

        mDayView = new DailyView(this);
        mDayView.setlist(mList);
        mDayView.setCallbackHandler(mAdapter);
        mLayout.addView(mDayView);
    }

    public interface CallBackInterface {
        void onViewItemClick(int itemNo);
    }

    private class CallBackAdapter implements CallBackInterface {
        public void onViewItemClick(int itemNo) {
            startEditTask(mList.get(itemNo).getId());
        }
    }
    CallBackAdapter mAdapter = new CallBackAdapter();

    private int ADD_TASK =111;
    private void startEditTask(final int _id) {
        Intent addTask = new Intent(this, AddEventActivity.class);
        addTask.putExtra("_id", _id);
        this.startActivityForResult(addTask, ADD_TASK);
    }
}
