
package com.woodduck.alarmme;

import com.woodduck.alarmme.audio.AudioPlayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class PlayReceiver extends BroadcastReceiver {
    String TAG = "PlayReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        if (bData.get("msg").equals("play_hskay")) {

            // launch intent service
            String recordPath = (String) bData.get("audio_path");
            Log.d(TAG,"do .. like play audio" + recordPath);
            if (recordPath != null && !recordPath.isEmpty()) {
                AudioPlayer mPlayer = new AudioPlayer();
                mPlayer.play(recordPath);
                Log.d(TAG,"play..........." + recordPath);
            }
            showDialog(context,intent);
        }
    }
    
    private void showDialog(Context context,Intent intent){
        EventItem item = (EventItem) intent.getSerializableExtra("event");
        Log.d(TAG,"show dialog with system alert");
        AlertDialog alert = new AlertDialog.Builder(context)
        .setTitle(item.getName())
        .setMessage(item.getDetail())
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"play...........ok");
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"play...........cancel");
            }
        }).create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }
}
