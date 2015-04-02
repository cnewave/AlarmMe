package com.woodduck.alarmme;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.*;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
    private String TAG = "AlarmMeMain";
    private ImageButton mRecording;
    private Button mAlarm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
    }
    
    private String[] mStrings = {"Abc","defdd","dddd"};
    private void initListView(){
        ListView mList = (ListView)findViewById(R.id.list);
        if(mList != null){
            // set adapter;
            Log.d(TAG, "set list adapter");

            mList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, mStrings));
            mRecording = (ImageButton)findViewById(R.id.recording);
            mRecording.setOnClickListener(new RecoringListener());
            
            mAlarm = (Button)findViewById(R.id.alarm_me);
            mAlarm.setOnClickListener(new View.OnClickListener() {                
                @Override
                public void onClick(View v) {
                    onClickSetup();
                    showDatePicker();
                }
            });
        }else{
            Log.e(TAG, "Not set list adapter");
        }
    }
    
    AudioRecorder recorder= new AudioRecorder();
    class RecoringListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do record button...");
            if(recorder.getRecordState()){
                recorder.stopRecording();
            }else{
                recorder.startRecording();
            }
            
        }
    }
    // alarm
    public void onClickSetup(){
        Log.d(TAG, "do onClickSetup..ddd.");
        Calendar cal = Calendar.getInstance();
        // 設定於 3 分鐘後執行
        //cal.add(Calendar.MINUTE, 1);
        // please not.. the month start from 0--> Jan 1-> Feb
        cal.set(2015, Calendar.APRIL, 02, 18, 22);
     
        Intent intent = new Intent(this, PlayReceiver.class);
        intent.putExtra("msg", "play_hskay");
     
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
             
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }
    
    // Note: Date picker has different display type..
    private DatePickerDialog showDatePicker(){
        Calendar dueDateCalendar = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(this, null, 
                dueDateCalendar.get(Calendar.YEAR), 
                dueDateCalendar.get(Calendar.MONTH), 
                dueDateCalendar.get(Calendar.DAY_OF_MONTH))
            {
                @Override
                protected void onCreate(Bundle savedInstanceState)
                {
                    super.onCreate(savedInstanceState);
                    int year = getContext().getResources()
                        .getIdentifier("android:id/year", null, null);
                    if(year != 0){
                        View yearPicker = findViewById(year);
                        if(yearPicker != null){
                            yearPicker.setVisibility(View.GONE);
                        }
                    }
                }
            };
            dlg.show();
            return dlg;
    }

}
