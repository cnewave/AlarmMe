
package com.woodduck.alarmme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.woodduck.alarmme.audio.AudioPlayer;
import com.woodduck.alarmme.audio.AudioRecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AudioFragment extends Fragment {
    private String TAG = "AudioFragment";
    private ImageButton mAudioRecord;
    private ImageButton mReplay;
    private String recordPath;

    public static AudioFragment newInstance() {
        AudioFragment f = new AudioFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        Log.d(TAG, "create view....");
        View view = inflater.inflate(R.layout.audiorecorder, container, false);
        mAudioRecord = (ImageButton) view.findViewById(R.id.recording);
        mAudioRecord.setOnClickListener(new RecoringListener());

        mReplay = (ImageButton) view.findViewById(R.id.replay);
        mReplay.setOnClickListener(new PlayListener());
        return view;
    }

    // 1. Show start...timer..
    // 2. Should use the thread, not use the UI thread..
    AudioRecorder recorder = new AudioRecorder();

    class RecoringListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do record button...");
            if (recorder.getRecordState()) {
                recorder.stopRecording();
            } else {
                mPlayer.stop();
                recordPath = getRecoringPath();
                recorder.startRecording(recordPath);
            }
        }
    }

    AudioPlayer mPlayer = new AudioPlayer();

    class PlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do replay button...");
            mPlayer.play(recordPath);
        }
    }

    // need to refine the path and file name.
    private String getRecoringPath() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = dateformat.format(calendar.getTime());

        File sdpath = Environment.getExternalStorageDirectory();

        File recordpath = new File(sdpath.getAbsolutePath() + "/record");
        if (!recordpath.exists())
            recordpath.mkdir();

        recordPath = recordpath.getAbsolutePath() + "/" + currentDateandTime + ".amr";
        Log.d(TAG, "get path:" + recordPath);
        return recordPath;
    }

    public String getRecordPath() {
        return recordPath;
    }
    public void setRecordPath(String path){
        recordPath = path;
    }
}
