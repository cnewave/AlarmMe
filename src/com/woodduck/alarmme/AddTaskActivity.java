
package com.woodduck.alarmme;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.woodduck.alarmme.database.ItemDAO;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// Here we will change AddTaskActivity to support edit/add task .
public class AddTaskActivity extends ActionBarActivity {
    private String TAG = "AlarmMeMain";
    private Button mDateButton;
    private Button mTimeButton;
    private Button mOKButton;
    private Button mCancelButton;
    private EditText mTitle;
    private EditText mDetail;
    private int queryID = 0;

    Calendar rightNow;
    AudioFragment mAudioFragment;
    VideoFragment mVideoFragment;
    RadioButton mAudioRadio;
    RadioButton mVideoRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.addalarmtask);
        initUI();
    }

    private void initFragments() {
        mVideoFragment = VideoFragment.newInstance();
        mAudioFragment = AudioFragment.newInstance();
    }

    private void initAudioFragement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mVideoFragment);
        ft.add(R.id.recorder_page, mAudioFragment);
        ft.commit();
    }

    private void initVideoFragement() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(mAudioFragment);
        ft.add(R.id.recorder_page, mVideoFragment);
        ft.commit();
    }

    private void initUI() {
        rightNow = Calendar.getInstance();
        initEditText();
        initFragments();
        // initAudioFragement();
        // initVideoFragement();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.add_actionbar);
        actionBar.show();
        getIntents();
        initButton();
        initRadioButtons();
    }

    private void initRadioButtons() {

        mAudioRadio = (RadioButton) findViewById(R.id.radio_audio);
        mAudioRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick audio fragment");
                initAudioFragement();
            }
        });
        mVideoRadio = (RadioButton) findViewById(R.id.radio_video);
        mVideoRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick video fragment");
                initVideoFragement();
            }
        });
        if (mAudioRadio.isChecked()) {
            initAudioFragement();
        } else {
            initVideoFragement();
        }
    }

    private void getIntents() {
        Intent intent = this.getIntent();
        if (intent != null) {
            queryID = intent.getIntExtra("_id", 0);
            Log.d(TAG, "query ID :" + queryID);
            if (queryID != 0) {
                ItemDAO readDAO = new ItemDAO(this);
                EventItem item = readDAO.get(queryID);
                Log.d(TAG, "Query item :" + item);
                mTitle.setText(item.getName());
                mDetail.setText(item.getDetail());
                String execute = item.getExecuteTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    rightNow.setTime(dateFormat.parse(execute));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String filePath = item.getVideoPath();
                if(filePath != null){
                    mVideoFragment.setRecordPath(filePath);
                }
                filePath = item.getAudioPath();
                if(filePath != null){
                    mAudioFragment.setRecordPath(filePath);
                }
            }
        } else {
            Log.d(TAG, "No inten.. add activity");
        }

    }

    private void initButton() {
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
    }

    private void initEditText() {
        mTitle = (EditText) findViewById(R.id.title);
        mDetail = (EditText) findViewById(R.id.details);
    }

    private void saveTask() {
        //
        // Create a thread to save to DB.
        createEventItem();
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
        // Test
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(rightNow.getTime());
        Log.d(TAG, "time -->:" + currentDateandTime);
        return today.toString();
    }

    private String getCurrentTime() {
        StringBuilder currentTime = new StringBuilder(rightNow.get(Calendar.HOUR_OF_DAY) + ":"
                + String.format("%02d", rightNow.get(Calendar.MINUTE)));

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

    private void createEventItem() {
        try {
            String title = mTitle.getText().toString();
            String detail = mDetail.getText().toString();

            EventItem item = new EventItem(title, detail, mAudioFragment.getRecordPath(),
                    mVideoFragment.getRecordPath(),
                    getDateTime(rightNow.getTime()));
            item.setId(queryID);
            Log.d(TAG, "createEventItem :" + item);
            // testDB(item);
            prepareAlarmManager(item);
            if (queryID == 0) {
                insertDB(item);
            } else {
                updateDB(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void insertDB(EventItem item) {
        ItemDAO test = new ItemDAO(this);
        test.insert(item);
    }

    private void updateDB(EventItem item) {
        ItemDAO test = new ItemDAO(this);
        test.update(item);
    }

    private void prepareAlarmManager(EventItem item) {
        Intent intent = new Intent(this, PlayReceiver.class);
        Bundle bundle = new Bundle();
        Log.d(TAG, "prepareAlarmManager.." + item);
        bundle.putString("msg", "play_hskay");
        bundle.putString("audio_path", item.getAudioPath());
        bundle.putSerializable("event", item);
        intent.putExtras(bundle);

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, rightNow.getTimeInMillis(), pi);
    }
}
