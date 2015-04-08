package com.woodduck.alarmme;

import com.woodduck.alarmme.audio.AudioPlayer;
import com.woodduck.alarmme.audio.AudioRecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AudioFragment extends Fragment {
    private String TAG = "AudioFragment";
    private ImageButton mAudioRecord;
    private ImageButton mReplay;

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
                recorder.startRecording();
            }
        }
    }

    AudioPlayer mPlayer = new AudioPlayer();

    class PlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do replay button...");
            mPlayer.play();
        }
    }
}
