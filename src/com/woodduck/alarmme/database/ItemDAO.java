
package com.woodduck.alarmme.database;

import com.woodduck.alarmme.EventItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public boolean update(EventItem item) {
        ContentValues cv = new ContentValues();
        cv.put(Detail.EVENT_NAME, item.getName());
        cv.put(Detail.EVENT_DETAIL, item.getDetail());
        cv.put(Detail.AUDIO_PATH, item.getAudioPath());
        cv.put(Detail.VIDEO_PATH, item.getVideoPath());
        cv.put(Detail.EXECUTE_TIME, item.getExecuteTime());
        String where = Detail.KEY_ID + "=" + item.getId();
        return db.update(Detail.TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(long id) {
        String where = Detail.KEY_ID + "=" + id;
        return db.delete(Detail.TABLE_NAME, where, null) > 0;
    }

    public EventItem get(long id) {
        EventItem item = null;
        String where = Detail.KEY_ID + "=" + id;

        Cursor result = db.query(
                Detail.TABLE_NAME, null, where, null, null, null, null, null);
        if (result.moveToFirst()) {
            item = getRecord(result);
        }
        result.close();
        // 回傳結果
        return item;
    }

    public EventItem getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        EventItem result = new EventItem();
        result.setId(cursor.getInt(0));
        result.setName(cursor.getString(1));
        result.setDetail(cursor.getString(2));
        result.setAudioPath(cursor.getString(3));
        result.setVideoPath(cursor.getString(4));
        result.setExecuteTime(cursor.getString(5));
        return result;
    }
}
