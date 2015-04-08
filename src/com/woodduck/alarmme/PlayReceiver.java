package com.woodduck.alarmme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PlayReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        if (bData.get("msg").equals("play_hskay")) {
            System.out.println("do something.. like play audio");
            // launch intent service
        }
    }
}
