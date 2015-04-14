
package com.woodduck.alarmme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private String TAG = "MyDBHelper";
    public static final String DATABASE_NAME = "events.db";
    public static final int VERSION = 1;
    private static SQLiteDatabase database;

    public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }

        return database;
    }

    public static class Detail {
        public static final String TABLE_NAME = "event_detail";
        public static final String KEY_ID = "_id";
        public static final String EVENT_NAME = "name";
        public static final String EVENT_DETAIL = "deatil";
        public static final String AUDIO_PATH = "audiopath";
        public static final String VIDEO_PATH = "videopath";
        public static final String EXECUTE_TIME = "execute_time";

        private static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EVENT_NAME + " TEXT,"
                + EVENT_DETAIL + " TEXT,"
                + AUDIO_PATH + " TEXT,"
                + VIDEO_PATH + " TEXT,"
                + EXECUTE_TIME + " DATETIME"
                + ")";
    }

    private void createDatabase(SQLiteDatabase db) {
        Log.i(TAG, "createDatabase");
        db.execSQL(Detail.CREATE_TABLE);
    }
}
