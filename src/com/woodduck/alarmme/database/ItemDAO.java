package com.woodduck.alarmme.database;

import com.woodduck.alarmme.EventItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.woodduck.alarmme.database.MyDBHelper.Detail;

public class ItemDAO {
    String TAG = "ItemDAO";
    private SQLiteDatabase db;

    public ItemDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    public void close() {
        db.close();
    }

    public long insert(EventItem item) {
        ContentValues cv = new ContentValues();
        cv.put(Detail.EVENT_NAME, item.getName());
        cv.put(Detail.EVENT_DETAIL, item.getDetail());
        cv.put(Detail.AUDIO_PATH, item.getAudioPath());
        cv.put(Detail.VIDEO_PATH, item.getVideoPath());
        cv.put(Detail.EXECUTE_TIME, item.getExecuteTime());

        long id = db.insert(Detail.TABLE_NAME, null, cv);

        Log.d(TAG, "insert result:" + id);
        return id;
    }
    
}